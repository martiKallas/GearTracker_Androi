package osu.kallasm.geartracker.Utils;

import android.text.InputFilter;
import android.text.Spanned;

//source: https://stackoverflow.com/questions/14212518/is-there-a-way-to-define-a-min-and-max-value-for-edittext-in-android
public class PercentFilter implements InputFilter {
    private int min, max;

    public PercentFilter(int min, int max){
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend){
        try {
            // Remove the string out of destination that is to be replaced
            String newVal = dest.toString().substring(0, dstart) + dest.toString().substring(dend, dest.toString().length());
            // Add the new string in
            newVal = newVal.substring(0, dstart) + source.toString() + newVal.substring(dstart, newVal.length());
            int input = Integer.parseInt(newVal);
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe){ return ""; }
        return "";
    }

    private boolean isInRange(int a, int b, int c){
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }


}
