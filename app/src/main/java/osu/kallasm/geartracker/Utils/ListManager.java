package osu.kallasm.geartracker.Utils;

import java.util.ArrayList;

import osu.kallasm.geartracker.DataModels.AttachmentData;
import osu.kallasm.geartracker.DataModels.WeaponData;

//Singleton to manage the Weapon and Attachments list for the app
//  Should allow for consistency between activities and reduce HTTP calls
public class ListManager {
    private static ListManager manager = null;
    private ArrayList<WeaponData> weaponList;
    private ArrayList<AttachmentData> attachmentList;

    private ListManager(){}

    synchronized public ListManager getListManager(){
        if (manager == null){
            manager = new ListManager();
        }
        return manager;
    }

    synchronized public void setWeaponList(ArrayList<WeaponData> list){ this.weaponList = list;}

    synchronized public void setAttachmentList(ArrayList<AttachmentData> list){ this.attachmentList = list;}

    synchronized public ArrayList<WeaponData> getWeaponList(){return weaponList;}

    synchronized public ArrayList<AttachmentData> getAttachmentList() {return attachmentList;}
}
