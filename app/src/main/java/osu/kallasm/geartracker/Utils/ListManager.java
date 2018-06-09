package osu.kallasm.geartracker.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import osu.kallasm.geartracker.DataModels.AttachmentData;
import osu.kallasm.geartracker.DataModels.WeaponData;
import osu.kallasm.geartracker.HomeActivity;
import osu.kallasm.geartracker.HttpHandler;
import osu.kallasm.geartracker.Interfaces.AttachmentListView;
import osu.kallasm.geartracker.Interfaces.WeaponListView;

//Singleton to manage the Weapon and Attachments list for the app
//  Should allow for consistency between activities and reduce HTTP calls
public class ListManager {
    private static ListManager manager = null;
    private static HttpHandler client;
    private ArrayList<WeaponData> weaponList = null;
    private ArrayList<AttachmentData> attachmentList = null;
    private ArrayList<WeaponListView> weaponListViews = null;
    private ArrayList<AttachmentListView> attachmentListViews = null;
    private HomeActivity home;
    private Lock attachmentLock = new ReentrantLock();
    private Lock weaponLock = new ReentrantLock();

    private ListManager(){}

    synchronized public static ListManager getListManager(HomeActivity home){
        if (manager == null){
            manager = new ListManager();
            manager.client = new HttpHandler();
            manager.weaponList = new ArrayList<>();
            manager.attachmentList = new ArrayList<>();
            manager.weaponListViews = new ArrayList<>();
            manager.attachmentListViews = new ArrayList<>();
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

    synchronized public void registerAttachmentListView(AttachmentListView view){
        attachmentListViews.add(view);
    }

    synchronized public void removeAttachmentListView(AttachmentListView view){
        attachmentListViews.remove(view);
    }

    public void setWeaponList(ArrayList<WeaponData> list){
        weaponLock.lock();
        this.weaponList = list;
        weaponLock.unlock();
    }

    public void refreshWeaponsLists(){
        weaponLock.lock();
        for (WeaponListView view : weaponListViews) {
            final WeaponListView v = view;
            //Source: CS496 Lecture - okhttp
            home.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.updateWeaponList(weaponList);
                }
            });
        }
        weaponLock.unlock();
    }

    public void setAttachmentList(ArrayList<AttachmentData> list){ this.attachmentList = list;}

    public void refreshAttachmentLists(){
        attachmentLock.lock();
        for (AttachmentListView view : attachmentListViews) {
            final AttachmentListView v = view;
            //Source: CS496 Lecture - okhttp
            home.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.updateAttachmentList(attachmentList);
                }
            });
        }
        attachmentLock.unlock();
    }

    private ArrayList<WeaponData> getWeaponList(){return weaponList;}

    public void copyWeapons(ArrayList<WeaponData> list){
        weaponLock.lock();
        for(WeaponData wpn : weaponList){
            list.add(wpn);
        }
        weaponLock.unlock();
    }

    private ArrayList<AttachmentData> getAttachmentList() {return attachmentList;}

    public void copyAttachments(ArrayList<AttachmentData> list){
        attachmentLock.lock();
        for(AttachmentData wpn : attachmentList){
            list.add(wpn);
        }
        attachmentLock.unlock();
    }

    public void getWeapons() throws IOException{
        client.getWeapons(this);
    }

    public void getAttachments() throws IOException{
        client.getAttachments(this);
    }

    public void addWeapon(WeaponData weapon) {
        client.addWeapon(this, weapon);
    }

    public void weaponAdded(WeaponData weapon){
        weaponLock.lock();
        weaponList.add(weapon);
        weaponLock.unlock();
        refreshWeaponsLists();
    }

    public void addAttachment(AttachmentData attachment) {
        client.addAttachment(this, attachment);
    }

    public void attachmentAdded(AttachmentData attachment){
        attachmentLock.lock();
        attachmentList.add(attachment);
        attachmentLock.unlock();
        refreshAttachmentLists();
    }

    public int getWeaponPosition(String id){
        weaponLock.lock();
        for(int i = 0; i < weaponList.size(); i++){
            if (weaponList.get(i).id.equals(id)){
                weaponLock.unlock();
                return i;
            }
        }
        weaponLock.unlock();
        return -1;
    }

    public void updateWeapon(WeaponData weapon){client.updateWeapon(this, weapon);}

    public void weaponUpdated(WeaponData weapon) {
        int position = getWeaponPosition(weapon.id);
        if (position >= 0) {
            weaponLock.lock();
            weaponList.set(position, weapon);
            weaponLock.unlock();
            refreshWeaponsLists();
        }
    }

    public void deleteWeapon(WeaponData weapon){
        client.deleteWeapon(this, weapon);
    }

    public void weaponDeleted(WeaponData weapon){
        int position = getWeaponPosition(weapon.id);
        if(position >= 0){
            weaponLock.lock();
            weaponList.remove(position);
            weaponLock.unlock();
            refreshWeaponsLists();
        }
    }

    public int getAttachmentPosition(String id){
        attachmentLock.lock();
        for(int i = 0; i < attachmentList.size(); i++){
            if (attachmentList.get(i).id.equals(id)){
                attachmentLock.unlock();
                return i;
            }
        }
        attachmentLock.unlock();
        return -1;
    }

    public void updateAttachment(AttachmentData attachment){client.updateAttachment(this, attachment);}

    public void attachmentUpdated(AttachmentData attachment){
        int position = getAttachmentPosition(attachment.id);
        if (position >= 0){
            attachmentLock.lock();
            attachmentList.set(position, attachment);
            attachmentLock.unlock();
            refreshAttachmentLists();
        }
    }

    public void deleteAttachment(AttachmentData attachment){
        client.deleteAttachment(this, attachment);
    }

    public void attachmentDeleted(AttachmentData attachment){
        int position = getAttachmentPosition(attachment.id);
        if(position >= 0){
            attachmentLock.lock();
            attachmentList.remove(position);
            attachmentLock.unlock();
            refreshAttachmentLists();
        }
    }
}
