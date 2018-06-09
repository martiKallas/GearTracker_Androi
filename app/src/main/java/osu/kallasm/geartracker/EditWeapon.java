package osu.kallasm.geartracker;

import android.content.Intent;
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

public class EditWeapon extends AppCompatActivity {

    Spinner firstTalent, secondTalent, freeTalent, attachment;
    EditText name, damage;
    WeaponData weapon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_weapon);
        Intent editIntent = getIntent();
        weapon = (WeaponData) editIntent.getSerializableExtra("weapon");
        System.out.println("Weapon name: " + weapon.name);
        System.out.println("Weapon id: " + weapon.id);

        String[] content = StaticLists.TALENTS;
        //Build the spinners and assign variables
        //Source: https://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, content);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstTalent = (Spinner)findViewById(R.id.editWeapon_firstTalent);
        firstTalent.setAdapter(adapter);
        secondTalent = (Spinner)findViewById(R.id.editWeapon_secondTalent);
        secondTalent.setAdapter(adapter);
        freeTalent = (Spinner)findViewById(R.id.editWeapon_freeTalent);
        freeTalent.setAdapter(adapter);

        name = (EditText)findViewById(R.id.editWeapon_name);
        damage = (EditText)findViewById(R.id.editWeapon_damage);
        damage.setFilters(new InputFilter[]{new PercentFilter(0, 100)});

        //Set attachment spinner
        attachment = (Spinner)findViewById(R.id.editWeapon_attachment);

        //Assign current values:
        name.setText(weapon.name);
        damage.setText(Integer.toString(weapon.damage));
        firstTalent.setSelection(StaticLists.getTalentPosition(weapon.firstTalent));
        secondTalent.setSelection(StaticLists.getTalentPosition(weapon.secondTalent));
        freeTalent.setSelection(StaticLists.getTalentPosition(weapon.freeTalent));
    }

    public void updateWeapon(View v){
        WeaponData newWeapon = new WeaponData();
        newWeapon.name = name.getText().toString();
        newWeapon.damage = Integer.parseInt(damage.getText().toString());
        newWeapon.firstTalent = firstTalent.getSelectedItem().toString();
        newWeapon.secondTalent = secondTalent.getSelectedItem().toString();
        newWeapon.freeTalent = freeTalent.getSelectedItem().toString();
        newWeapon.attachment = null;
        HttpHandler handler = new HttpHandler();
        //handler.editWeapon(newWeapon);
    }
}
