package osu.kallasm.geartracker.Utils;

import java.io.IOException;
import java.util.ArrayList;

import osu.kallasm.geartracker.DataModels.AttachmentData;
import osu.kallasm.geartracker.DataModels.WeaponData;
import osu.kallasm.geartracker.HomeActivity;
import osu.kallasm.geartracker.HttpHandler;
import osu.kallasm.geartracker.Interfaces.WeaponListView;

//Singleton to manage the Weapon and Attachments list for the app
//  Should allow for consistency between activities and reduce HTTP calls
public class ListManager {
    private static ListManager manager = null;
    private static HttpHandler client;
    private ArrayList<WeaponData> weaponList = null;
    private ArrayList<AttachmentData> attachmentList = null;
    private ArrayList<WeaponListView> weaponListViews = null;
    private HomeActivity home;

    private ListManager(){}

    synchronized public static ListManager getListManager(HomeActivity home){
        if (manager == null){
            manager = new ListManager();
            manager.client = new HttpHandler();
            manager.weaponList = new ArrayList<>();
            manager.attachmentList = new ArrayList<>();
            manager.weaponListViews = new ArrayList<>();
            manager.home = home;
        }
        return manager;
    }

    synchronized public void registerWeaponListView(WeaponListView view){
        weaponListViews.add(view);
    }

    synchronized public void removeWeaponListView(WeaponListView view){
        weaponListViews.remove(view);
    }

    synchronized public void setWeaponList(ArrayList<WeaponData> list){
        this.weaponList = list;
    }

    synchronized public void refreshWeaponsLists(){
        for (WeaponListView view : weaponListViews) {
            final WeaponListView v = view;
            //Source: CS496 Lecture - okhttp
            home.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.updateList(weaponList);
                }
            });
        }
    }

    synchronized public void setAttachmentList(ArrayList<AttachmentData> list){ this.attachmentList = list;}

    synchronized public ArrayList<WeaponData> getWeaponList(){return weaponList;}

    synchronized public void copyWeapons(ArrayList<WeaponData> list){
        for(WeaponData wpn : weaponList){
            list.add(wpn);
        }
    }

    synchronized public ArrayList<AttachmentData> getAttachmentList() {return attachmentList;}

    public void getWeapons() throws IOException{
        client.getWeapons(this);
    }

    public void addWeapon(WeaponData weapon){
        client.addWeapon(this, weapon);
    }

    synchronized public void weaponAdded(WeaponData weapon){
        weaponList.add(weapon);
        refreshWeaponsLists();
    }
}
