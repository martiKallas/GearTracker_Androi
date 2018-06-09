package osu.kallasm.geartracker.DataModels;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class AttachmentData implements Serializable{
    @Expose
    public String name;
    @Expose
    public String primaryAttribute;
    @Expose
    public int primaryValue;
    @Expose
    public String secondaryAttribute;
    @Expose
    public int secondaryValue;
    @Expose
    public String attached_to = null;
    public String self = null;
    public String id = null;

    public AttachmentData(){
        super();
        attached_to = null;
        self = null;
        id = null;
    }
}
