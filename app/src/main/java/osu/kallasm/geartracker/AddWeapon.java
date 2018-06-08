package osu.kallasm.geartracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import osu.kallasm.geartracker.DataModels.WeaponData;
import osu.kallasm.geartracker.Utils.PercentFilter;
import osu.kallasm.geartracker.Utils.StaticLists;

public class AddWeapon extends AppCompatActivity {

    Spinner firstTalent, secondTalent, freeTalent;
    EditText name, damage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weapon);
        String[] content = StaticLists.TALENTS;
        //Source: https://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, content);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstTalent = (Spinner)findViewById(R.id.addWeapon_firstTalent);
        firstTalent.setAdapter(adapter);
        secondTalent = (Spinner)findViewById(R.id.addWeapon_secondTalent);
        secondTalent.setAdapter(adapter);
        freeTalent = (Spinner)findViewById(R.id.addWeapon_freeTalent);
        freeTalent.setAdapter(adapter);
        name = (EditText)findViewById(R.id.addWeapon_name);
        damage = (EditText)findViewById(R.id.addWeapon_damage);
        damage.setFilters(new InputFilter[]{new PercentFilter(0, 100)});
    }

    public void addWeapon(View v){
        WeaponData newWeapon = new WeaponData();
        newWeapon.name = name.getText().toString();
        newWeapon.damage = Integer.parseInt(damage.getText().toString());
        newWeapon.firstTalent = firstTalent.getSelectedItem().toString();
        newWeapon.secondTalent = secondTalent.getSelectedItem().toString();
        newWeapon.freeTalent = freeTalent.getSelectedItem().toString();
        newWeapon.attachment = null;
        HttpHandler handler = new HttpHandler();
        handler.addWeapon(newWeapon);
    }
}
