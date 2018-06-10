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
import osu.kallasm.geartracker.Interfaces.AttachmentSpinnerView;
import osu.kallasm.geartracker.Interfaces.WeaponListView;
import osu.kallasm.geartracker.Interfaces.WeaponSpinnerView;

//Singleton to manage the Weapon and Attachments list for the app
//  Should allow for consistency between activities and reduce HTTP calls
public class ListManager {
    private static ListManager manager = null;
    private static HttpHandler client;
    private ArrayList<WeaponData> weaponList = null;
    private ArrayList<AttachmentData> attachmentList = null;
    private ArrayList<WeaponListView> weaponListViews = null;
    private ArrayList<WeaponSpinnerView>  weaponSpinnerViews = null;
    private ArrayList<AttachmentListView> attachmentListViews = null;
    private ArrayList<AttachmentSpinnerView> attachmentSpinnerViews = null;
    private ArrayList<String> attachmentSpinnerList = null;
    private ArrayList<String> weaponSpinnerList = null;
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
            manager.weaponSpinnerViews = new ArrayList<>();
            manager.attachmentListViews = new ArrayList<>();
            manager.attachmentSpinnerViews = new ArrayList<>();
            manager.attachmentSpinnerList = new ArrayList<>();
            manager.attachmentSpinnerList.add("None");
            manager.weaponSpinnerList = new ArrayList<>();
            manager.weaponSpinnerList.add("None");
            manager.home = home;
        }
        return manager;
    }


    synchronized public void registerWeaponListView(WeaponListView view){
        weaponListViews.add(view);
    }

    synchronized  public void registerWeaponSpinnerView(WeaponSpinnerView view){
        weaponSpinnerViews.add(view);
    }

    synchronized public void removeWeaponListView(WeaponListView view){
        weaponListViews.remove(view);
    }

    synchronized public void removeWeaponSpinnerView(WeaponSpinnerView view){
        weaponSpinnerViews.remove(view);
    }

    synchronized public void registerAttachmentListView(AttachmentListView view){
        attachmentListViews.add(view);
    }

    synchronized public void registerAttachmentSpinnerView(AttachmentSpinnerView view){
        attachmentSpinnerViews.add(view);
    }

    synchronized public void removeAttachmentListView(AttachmentListView view){
        attachmentListViews.remove(view);
    }

    synchronized public void removeAttachmentSpinnerView(AttachmentSpinnerView view){
        attachmentSpinnerViews.remove(view);
    }

    public void setWeaponList(ArrayList<WeaponData> list){
        weaponLock.lock();
        this.weaponList = list;
        weaponSpinnerList.clear();
        weaponSpinnerList.add("None");
        for(WeaponData wpn : weaponList){
            addWeaponToSpinner(wpn);
        }
        weaponLock.unlock();
        refreshWeaponsLists();
        refreshWeaponSpinnerLists();
    }

    private void refreshWeaponsLists(){
        for (WeaponListView view : weaponListViews) {
            final WeaponListView v = view;
            //Source: CS496 Lecture - okhttp
            home.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.updateWeaponList();
                }
            });
        }
    }

    private void refreshWeaponSpinnerLists(){
        for (WeaponSpinnerView view : weaponSpinnerViews) {
            final WeaponSpinnerView v = view;
            //Source: CS496 Lecture - okhttp
            home.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.updateWeaponSpinnerList();
                }
            });
        }
    }

    public void setAttachmentList(ArrayList<AttachmentData> list){
        attachmentLock.lock();
        this.attachmentList = list;
        attachmentSpinnerList.clear();
        attachmentSpinnerList.add("None");
        for(AttachmentData attch : attachmentList){
            addAttachmentToSpinner(attch);
        }
        attachmentLock.unlock();
        refreshAttachmentsLists();
        refreshAttachmentSpinnerLists();
    }

    private void refreshAttachmentsLists(){
        for (AttachmentListView view : attachmentListViews) {
            final AttachmentListView v = view;
            //Source: CS496 Lecture - okhttp
            home.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.updateAttachmentList();
                }
            });
        }
    }

    private void refreshAttachmentSpinnerLists(){
        for (AttachmentSpinnerView view : attachmentSpinnerViews) {
            final AttachmentSpinnerView v = view;
            //Source: CS496 Lecture - okhttp
            home.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.updateAttachmentSpinnerList();
                }
            });
        }
    }

    private ArrayList<WeaponData> getWeaponList(){return weaponList;}

    public void copyWeapons(ArrayList<WeaponData> list){
        weaponLock.lock();
        for(WeaponData wpn : weaponList){
            list.add(wpn);
        }
        weaponLock.unlock();
    }

    public void copyWeaponSpinner(ArrayList<String> list){
        weaponLock.lock();
        for(String wpn : weaponSpinnerList){
            list.add(wpn);
        }
        weaponLock.unlock();
    }

    public void copyAttachmentSpinner(ArrayList<String> list){
        attachmentLock.lock();
        for(String wpn : attachmentSpinnerList){
            list.add(wpn);
        }
        attachmentLock.unlock();
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
        addWeaponToSpinner(weapon);
        weaponLock.unlock();
        refreshWeaponsLists();
    }

    public void addAttachment(AttachmentData attachment) {
        client.addAttachment(this, attachment);
    }

    public void attachmentAdded(AttachmentData attachment){
        attachmentLock.lock();
        attachmentList.add(attachment);
        addAttachmentToSpinner(attachment);
        attachmentLock.unlock();
        refreshAttachmentsLists();
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
            weaponSpinnerList.set(position+1, getWeaponSpinnerString(weapon));
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
            weaponSpinnerList.remove(position + 1);
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
            attachmentSpinnerList.set(position+1, getAttachmentSpinnerString(attachment));
            attachmentLock.unlock();
            refreshAttachmentsLists();
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
            attachmentSpinnerList.remove(position + 1);
            attachmentLock.unlock();
            refreshAttachmentsLists();
        }
    }

    private String getWeaponSpinnerString(WeaponData weapon){
        String spinnerData = weapon.name + ":  " + weapon.damage + "% dmg";
        return spinnerData;
    }

    private void addWeaponToSpinner(WeaponData weapon){
        weaponSpinnerList.add(getWeaponSpinnerString(weapon));
    }

    private String getAttachmentSpinnerString(AttachmentData attachment){
        String spinnerData = attachment.name + "- " + attachment.primaryAttribute + ", " + attachment.secondaryAttribute;
        return spinnerData;
    }

    private void addAttachmentToSpinner(AttachmentData attachment){
        attachmentSpinnerList.add(getAttachmentSpinnerString(attachment));
    }
}
