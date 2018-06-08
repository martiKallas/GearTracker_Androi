package osu.kallasm.geartracker;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import osu.kallasm.geartracker.Adapters.WeaponAdapter;
import osu.kallasm.geartracker.DataModels.WeaponData;

public class HttpHandler {
    private String url = "http://cs496-finalproject-geartracker.appspot.com";
    private OkHttpClient client = new OkHttpClient();
    private Gson exGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    //Returns 0 on success. Fills in weaponList with weapons from server
    public int getWeapons(final WeaponsList parent) throws IOException {
        Request request = new Request.Builder()
                .url(url + "/weapons")
                .build();
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
                System.out.println("TODO: Ouch");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int status = response.code();
                if (status >= 200 && status < 400) {
                    String res = response.body().string();
                    WeaponData[] responseArray = exGson.fromJson(res, WeaponData[].class);
                    System.out.println("Size of list: " + responseArray.length);
                    final ArrayList<WeaponData> responseList = new ArrayList<>();
                    for (WeaponData weapon : responseArray) {
                        System.out.println("Next weapon: " + weapon.name);
                        responseList.add(weapon);
                    }
                    //Source: CS496 Lecture - okhttp
                    parent.runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            parent.updateList(responseList);
                        }
                    });
                } else {
                    System.out.println("Call to server failed. Code: " + status);
                }
            }
        });

        return 0;
    }

}
