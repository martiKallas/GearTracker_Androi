package osu.kallasm.geartracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import osu.kallasm.geartracker.DataModels.WeaponData;
import osu.kallasm.geartracker.Utils.ListManager;
import osu.kallasm.geartracker.Utils.PercentFilter;
import osu.kallasm.geartracker.Utils.StaticLists;

public class AddWeapon extends AppCompatActivity {

    Spinner firstTalent, secondTalent, freeTalent;
    EditText name, damage;
    ListManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weapon);

        manager = ListManager.getListManager(null);

        //find views
        firstTalent = (Spinner)findViewById(R.id.addWeapon_firstTalent);
        secondTalent = (Spinner)findViewById(R.id.addWeapon_secondTalent);
        freeTalent = (Spinner)findViewById(R.id.addWeapon_freeTalent);
        name = (EditText)findViewById(R.id.addWeapon_name);
        damage = (EditText)findViewById(R.id.addWeapon_damage);

        //assign spinner values
        String[] content = StaticLists.TALENTS;
        //Source: https://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, content);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstTalent.setAdapter(adapter);
        secondTalent.setAdapter(adapter);
        freeTalent.setAdapter(adapter);

        //set filter on damage input
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
        manager.addWeapon(newWeapon);
    }
}
