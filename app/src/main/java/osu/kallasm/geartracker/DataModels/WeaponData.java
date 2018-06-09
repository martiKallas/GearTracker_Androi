package osu.kallasm.geartracker.DataModels;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

//For serializable usage: https://stackoverflow.com/questions/3323074/android-difference-between-parcelable-and-serializable
public class WeaponData implements Serializable {
    @Expose
    public String name;
    @Expose
    public int damage;
    @Expose
    public String firstTalent;
    @Expose
    public String secondTalent;
    @Expose
    public String freeTalent;
    @Expose
    public String attachment = null;
    public String self = null;
    public String id = null;

    public WeaponData(){
        super();
        attachment = null;
        self = null;
        id = null;
    }
}
