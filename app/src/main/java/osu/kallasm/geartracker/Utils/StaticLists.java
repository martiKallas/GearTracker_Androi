package osu.kallasm.geartracker.Utils;

public class StaticLists {
    public static int getTalentPosition(String talent){
        for(int i = 0; i < TALENTS.length; i++){
            if (TALENTS[i].equals(talent)) return i;
        }
        return -1;
    }

    public static int getAttributePosition(String talent){
        for(int i = 0; i < ATTRIBUTES.length; i++){
            if (ATTRIBUTES[i].equals(talent)) return i;
        }
        return -1;
    }

    public final static String[] TALENTS = {
            "Accurate",
            "Adept",
            "Balanced",
            "Brutal",
            "Capable",
            "Competent",
            "Deadly",
            "Disciplined",
            "Destructive",
            "Determined",
            "Elevated",
            "Ferocious",
            "Fierce",
            "Focused",
            "Meticulous",
            "Predatory",
            "Prepared",
            "Proficient",
            "Responsive",
            "Restored",
            "Self-preserved",
            "Stable",
            "Sustained",
            "Swift",
            "Talented",
            "Unforgiving",
            "Vicious"
    };

    public final static String[] ATTRIBUTES = {
            "Accuracy",
            "Crit Chance",
            "Crit Damage",
            "Headshot Dmg",
            "Rate of Fire",
            "Reload Speed",
            "Stability"
    };
}
