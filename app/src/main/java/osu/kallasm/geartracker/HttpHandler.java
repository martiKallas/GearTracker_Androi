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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import osu.kallasm.geartracker.Adapters.WeaponAdapter;
import osu.kallasm.geartracker.DataModels.WeaponData;
import osu.kallasm.geartracker.Utils.ListManager;

public class HttpHandler {
    private String url = "http://cs496-finalproject-geartracker.appspot.com";
    private OkHttpClient client = new OkHttpClient();
    private Gson exGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private Gson gson = new GsonBuilder().create();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //Returns 0 on success. Fills in weaponList with weapons from server
    public void getWeapons(final ListManager parent) throws IOException {
        Request request = new Request.Builder()
                .url(url + "/weapons")
                .build();
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
                System.out.println("TODO: Failed in getWeapons");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int status = response.code();
                if (status >= 200 && status < 400) {
                    String res = response.body().string();
                    WeaponData[] responseArray = gson.fromJson(res, WeaponData[].class);
                    System.out.println("Size of list: " + responseArray.length);
                    final ArrayList<WeaponData> responseList = new ArrayList<>();
                    for (WeaponData weapon : responseArray) {
                        System.out.println("Weapon: " + weapon.name);
                        System.out.println("ID: " + weapon.id);
                        responseList.add(weapon);
                    }
                    parent.setWeaponList(responseList);
                    parent.refreshWeaponsLists();
                } else {
                    System.out.println("Bad response in getWeapons. Code: " + status);
                }
            }
        });
    }

    public void addWeapon(final ListManager manager, WeaponData weapon){
        String json = exGson.toJson(weapon);
        System.out.println("Log weapon: " + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url + "/weapons")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
                System.out.println("TODO: Failed in addWeapon");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException{
                int status = response.code();
                if (status >= 200 && status < 400){
                    System.out.println("TODO: good response in addWeapon. Code: " + status);
                    String res = response.body().string();
                    WeaponData newWeapon = gson.fromJson(res, WeaponData.class);
                    manager.weaponAdded(newWeapon);
                }
                else{
                    System.out.println("TODO: bad response in addWeapon. Code: " + status);
                }
            }
        });
    }


}
