package osu.kallasm.geartracker.Utils;

import android.support.v7.app.AppCompatActivity;

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
    private class WeaponListPair {
        WeaponListView view;
        AppCompatActivity activity;
        WeaponListPair(WeaponListView view, AppCompatActivity activity){
            this.view = view;
            this.activity = activity;
        }
    }

    private class WeaponSpinnerPair {
        WeaponSpinnerView view;
        AppCompatActivity activity;
        WeaponSpinnerPair(WeaponSpinnerView view, AppCompatActivity activity){
            this.view = view;
            this.activity = activity;
        }
    }

    private class AttachmentListPair {
        AttachmentListView view;
        AppCompatActivity activity;
        AttachmentListPair(AttachmentListView view, AppCompatActivity activity){
            this.view = view;
            this.activity = activity;
        }
    }

    private class AttachmentSpinnerPair {
        AttachmentSpinnerView view;
        AppCompatActivity activity;
        AttachmentSpinnerPair(AttachmentSpinnerView view, AppCompatActivity activity){
            this.view = view;
            this.activity = activity;
        }
    }

    private static ListManager manager = null;
    private static HttpHandler client;
    private ArrayList<WeaponData> weaponList = null;
    private ArrayList<AttachmentData> attachmentList = null;
    private ArrayList<WeaponListPair> weaponListViews = null;
    private ArrayList<WeaponSpinnerPair>  weaponSpinnerViews = null;
    private ArrayList<AttachmentListPair> attachmentListViews = null;
    private ArrayList<AttachmentSpinnerPair> attachmentSpinnerViews = null;
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


    synchronized public void registerWeaponListView(WeaponListView view, AppCompatActivity activity){
        weaponListViews.add(new WeaponListPair(view, activity));
    }

    synchronized  public void registerWeaponSpinnerView(WeaponSpinnerView view, AppCompatActivity activity){
        weaponSpinnerViews.add(new WeaponSpinnerPair(view, activity));
    }

    synchronized public void removeWeaponListView(WeaponListView view){
        for (WeaponListPair pair : weaponListViews){
            if (pair.view == view){
                weaponListViews.remove(pair);
                return;
            }
        }
    }

    synchronized public void removeWeaponSpinnerView(WeaponSpinnerView view){
        for (WeaponSpinnerPair pair : weaponSpinnerViews){
            if (pair.view == view){
                weaponSpinnerViews.remove(pair);
                return;
            }
        }
    }

    synchronized public void registerAttachmentListView(AttachmentListView view, AppCompatActivity activity){
        attachmentListViews.add(new AttachmentListPair(view, activity));
    }

    synchronized public void registerAttachmentSpinnerView(AttachmentSpinnerView view, AppCompatActivity activity){
        attachmentSpinnerViews.add(new AttachmentSpinnerPair(view, activity));
    }

    synchronized public void removeAttachmentListView(AttachmentListView view){
        for (AttachmentListPair pair : attachmentListViews){
            if (pair.view == view){
                attachmentListViews.remove(pair);
                return;
            }
        }
    }

    synchronized public void removeAttachmentSpinnerView(AttachmentSpinnerView view){
        for (AttachmentSpinnerPair pair : attachmentSpinnerViews){
            if (pair.view == view){
                attachmentSpinnerViews.remove(pair);
                return;
            }
        }
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
        System.out.println("refreshWeaponsList called size: " + weaponListViews.size());
        for (WeaponListPair pair : weaponListViews) {
            System.out.println("found a pair in refreshWeaponsList");
            final WeaponListView v = pair.view;
            //Source: CS496 Lecture - okhttp
            pair.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Refresh weapons");
                    v.updateWeaponList();
                }
            });
        }
    }

    private void refreshWeaponSpinnerLists(){
        for (WeaponSpinnerPair pair : weaponSpinnerViews) {
            final WeaponSpinnerView v = pair.view;
            //Source: CS496 Lecture - okhttp
            pair.activity.runOnUiThread(new Runnable() {
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
        for (AttachmentListPair pair : attachmentListViews) {
            final AttachmentListView v = pair.view;
            //Source: CS496 Lecture - okhttp
            pair.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.updateAttachmentList();
                }
            });
        }
    }

    private void refreshAttachmentSpinnerLists(){
        for (AttachmentSpinnerPair pair : attachmentSpinnerViews) {
            final AttachmentSpinnerView v = pair.view;
            //Source: CS496 Lecture - okhttp
            pair.activity.runOnUiThread(new Runnable() {
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
        for(int i = 0; i < weaponList.size(); i++){
            if (weaponList.get(i).id.equals(id)){
                return i;
            }
        }
        return -1;
    }

    public void updateWeapon(WeaponData weapon){client.updateWeapon(this, weapon);}

    public void weaponUpdated(WeaponData weapon) {
        System.out.printf("Weapon %s: %d damage. Attached: %s\n", weapon.name, weapon.damage, weapon.attachment);
        int position = getWeaponPosition(weapon.id);
        if (position >= 0) {
            weaponLock.lock();
            String prevAttId = weaponList.get(position).attachment;
            attachmentLock.lock();
            //if new attachment is not the same as old attachment
            if (prevAttId != null && !prevAttId.equals(weapon.attachment)){
                //Remove weapon from old attachment
                int prevAttPos = getAttachmentPosition(prevAttId);
                AttachmentData prevAtt = attachmentList.get(prevAttPos);
                prevAtt.attached_to = null;
                attachmentList.set(prevAttPos, prevAtt);
            }
            //update new attachment
            if (weapon.attachment != null){
                int newAttPos = getAttachmentPosition(weapon.attachment);
                AttachmentData newAtt = attachmentList.get(newAttPos);
                //if new attachment used to have a weapon, update that:
                if (newAtt.attached_to != null){
                    int oldWeapPos = getWeaponPosition(newAtt.attached_to);
                    WeaponData old = weaponList.get(oldWeapPos);
                    old.attachment = null;
                    weaponList.set(oldWeapPos, old);
                }
                newAtt.attached_to = weapon.id;
                attachmentList.set(newAttPos, newAtt);
            }
            attachmentLock.unlock();
            weaponList.set(position, weapon);
            weaponSpinnerList.set(position+1, getWeaponSpinnerString(weapon));
            weaponLock.unlock();
            refreshWeaponsLists();
            refreshAttachmentsLists();
        }
    }

    public void deleteWeapon(WeaponData weapon){
        client.deleteWeapon(this, weapon);
    }

    public void weaponDeleted(WeaponData weapon){
        int position = getWeaponPosition(weapon.id);
        if(position >= 0){
            String prevAttId = weaponList.get(position).attachment;
            if (prevAttId != null){
                attachmentLock.lock();
                int prevAttPos = getAttachmentPosition(prevAttId);
                AttachmentData prevAtt = attachmentList.get(prevAttPos);
                prevAtt.attached_to = null;
                attachmentList.set(prevAttPos, prevAtt);
                attachmentLock.unlock();
            }
            weaponLock.lock();
            weaponList.remove(position);
            weaponSpinnerList.remove(position + 1);
            weaponLock.unlock();
            refreshWeaponsLists();
            refreshAttachmentsLists();
        }
    }

    public int getAttachmentPosition(String id){
        for(int i = 0; i < attachmentList.size(); i++){
            if (attachmentList.get(i).id.equals(id)){
                return i;
            }
        }
        return -1;
    }

    public void updateAttachment(AttachmentData attachment){client.updateAttachment(this, attachment);}

    public void attachmentUpdated(AttachmentData attachment){
        int position = getAttachmentPosition(attachment.id);
        if (position >= 0){
            String prevWeapId = attachmentList.get(position).attached_to;
            weaponLock.lock();
            //new weapon is not the same as old
            if (prevWeapId != null && !prevWeapId.equals(attachment.attached_to)){
                //Remove attachment from previous weapon
                int prevWeapPos = getWeaponPosition(prevWeapId);
                WeaponData prevWeap = weaponList.get(prevWeapPos);
                prevWeap.attachment = null;
                weaponList.set(prevWeapPos, prevWeap);

            }
            //update new weapon
            if (attachment.attached_to != null){
                int newWeapPos = getWeaponPosition(attachment.attached_to);
                WeaponData newWeap = weaponList.get(newWeapPos);
                //if new weapon had an old attachment, update that too
                if (newWeap.attachment != null){
                    int oldAttPos = getAttachmentPosition(newWeap.attachment);
                    AttachmentData old = attachmentList.get(oldAttPos);
                    old.attached_to = null;
                    attachmentList.set(oldAttPos, old);
                }
                newWeap.attachment = attachment.id;
                weaponList.set(newWeapPos, newWeap);
            }
            weaponLock.unlock();
            attachmentLock.lock();
            attachmentList.set(position, attachment);
            attachmentSpinnerList.set(position+1, getAttachmentSpinnerString(attachment));
            attachmentLock.unlock();
            refreshAttachmentsLists();
            refreshWeaponsLists();
        }
    }

    public void deleteAttachment(AttachmentData attachment){
        client.deleteAttachment(this, attachment);
    }

    public void attachmentDeleted(AttachmentData attachment){
        int position = getAttachmentPosition(attachment.id);
        if(position >= 0){
            String prevWeapId = attachmentList.get(position).attached_to;
            if (prevWeapId != null){
                weaponLock.lock();
                int prevWeapPos = getWeaponPosition(prevWeapId);
                WeaponData prevWeap = weaponList.get(prevWeapPos);
                prevWeap.attachment = null;
                weaponList.set(prevWeapPos,prevWeap);
                weaponLock.unlock();
            }
            attachmentLock.lock();
            attachmentList.remove(position);
            attachmentSpinnerList.remove(position + 1);
            attachmentLock.unlock();
            refreshAttachmentsLists();
            refreshWeaponsLists();
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
        String spinnerData = attachment.name + ":  " + attachment.primaryAttribute + ", " + attachment.secondaryAttribute;
        return spinnerData;
    }

    private void addAttachmentToSpinner(AttachmentData attachment){
        attachmentSpinnerList.add(getAttachmentSpinnerString(attachment));
    }

    public String getWeaponSpinnerString(int position){
        if (weaponSpinnerList.size() > position){
            return weaponSpinnerList.get(position);
        }
        return null;
    }

    public String getAttachmentSpinnerString(int position){
        if (attachmentSpinnerList.size() > position){
            return attachmentSpinnerList.get(position);
        }
        return null;
    }

    public String getWeaponId(int position){
        if (weaponList.size() > position){
            return weaponList.get(position).id;
        }
        return null;
    }

    public String getAttachmentId(int position){
        if (attachmentList.size() > position){
            return attachmentList.get(position).id;
        }
        return null;
    }
}
