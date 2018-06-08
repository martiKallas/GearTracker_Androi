package osu.kallasm.geartracker.DataModels;

import com.google.gson.annotations.Expose;

public class WeaponData {
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
    public String attachment;
    public String self;
    @Expose
    public String id;
}
