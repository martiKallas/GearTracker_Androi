package osu.kallasm.geartracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import osu.kallasm.geartracker.DataModels.AttachmentData;
import osu.kallasm.geartracker.DataModels.WeaponData;
import osu.kallasm.geartracker.Utils.ListManager;

public class HttpHandler {
    private String url = "http://cs496-finalproject-geartracker.appspot.com";
    private OkHttpClient client = new OkHttpClient();
    private Gson exGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private Gson gson = new GsonBuilder().create();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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
                    if (responseList.size() <= 0) return;
                    parent.setWeaponList(responseList);
                } else {
                    System.out.println("Bad response in getWeapons. Code: " + status);
                }
            }
        });
    }

    public void getAttachments(final ListManager parent) throws IOException {
        Request request = new Request.Builder()
                .url(url + "/attachments")
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
                    AttachmentData[] responseArray = gson.fromJson(res, AttachmentData[].class);
                    System.out.println("Size of list: " + responseArray.length);
                    final ArrayList<AttachmentData> responseList = new ArrayList<>();
                    for (AttachmentData attachment : responseArray) {
                        //System.out.println("Attachment: " + attachment.name);
                        //System.out.println("ID: " + attachment.id);
                        responseList.add(attachment);
                    }
                    if (responseList.size() <= 0) return;
                    parent.setAttachmentList(responseList);
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


    public void addAttachment(final ListManager manager, AttachmentData attachment){
        String json = exGson.toJson(attachment);
        System.out.println("Log attachment: " + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url + "/attachments")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
                System.out.println("TODO: Failed in addAttachment");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException{
                int status = response.code();
                if (status >= 200 && status < 400){
                    System.out.println("TODO: good response in addAttachment. Code: " + status);
                    String res = response.body().string();
                    AttachmentData newAttachment = gson.fromJson(res, AttachmentData.class);
                    manager.attachmentAdded(newAttachment);
                }
                else{
                    System.out.println("TODO: bad response in addAttachment. Code: " + status);
                }
            }
        });
    }

    public void updateWeapon(final ListManager manager, final WeaponData weapon){
        String json = gson.toJson(weapon);
        System.out.println("Updated weapon: " + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url + "/weapons/" + weapon.id)
                .put(body)
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
                    System.out.println("TODO: good response in updateWeapon. Code: " + status);
                    manager.weaponUpdated(weapon);
                }
                else{
                    System.out.println("TODO: bad response in updateWeapon. Code: " + status);
                }
            }
        });
    }

    public void deleteWeapon(final ListManager manager, final WeaponData weapon){
        String json = gson.toJson(weapon);
        System.out.println("Deleting weapon: " + json);
        Request request = new Request.Builder()
                .url(url + "/weapons/" + weapon.id)
                .delete()
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
                    System.out.println("TODO: good response in deleteWeapon. Code: " + status);
                    manager.weaponDeleted(weapon);
                }
                else{
                    System.out.println("TODO: bad response in deleteWeapon. Code: " + status);
                }
            }
        });
    }

    public void updateAttachment(final ListManager manager, final AttachmentData attachment){
        String json = gson.toJson(attachment);
        System.out.println("Updated attachment: " + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url + "/attachments/" + attachment.id)
                .put(body)
                .build();
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
                System.out.println("TODO: Failed in addAttachment");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException{
                int status = response.code();
                if (status >= 200 && status < 400){
                    System.out.println("TODO: good response in updateAttachment. Code: " + status);
                    manager.attachmentUpdated(attachment);
                }
                else{
                    System.out.println("TODO: bad response in updateAttachment. Code: " + status);
                }
            }
        });
    }

    public void deleteAttachment(final ListManager manager, final AttachmentData attachment){
        String json = gson.toJson(attachment);
        System.out.println("Deleting attachment: " + json);
        Request request = new Request.Builder()
                .url(url + "/attachments/" + attachment.id)
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
                System.out.println("TODO: Failed in addAttachment");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException{
                int status = response.code();
                if (status >= 200 && status < 400){
                    System.out.println("TODO: good response in deleteAttachment. Code: " + status);
                    manager.attachmentDeleted(attachment);
                }
                else{
                    System.out.println("TODO: bad response in deleteAttachment. Code: " + status);
                }
            }
        });
    }

}
