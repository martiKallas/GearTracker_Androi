package osu.kallasm.geartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import osu.kallasm.geartracker.DataModels.WeaponData;
import osu.kallasm.geartracker.Interfaces.AttachmentSpinnerView;
import osu.kallasm.geartracker.Utils.ListManager;
import osu.kallasm.geartracker.Utils.PercentFilter;
import osu.kallasm.geartracker.Utils.StaticLists;

public class EditWeapon extends AppCompatActivity implements AttachmentSpinnerView {

    Spinner firstTalent, secondTalent, freeTalent, attachment;
    EditText name, damage;
    WeaponData weapon;
    ArrayList<String> attachmentSpinnerList;
    ListManager manager = ListManager.getListManager(null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_weapon);
        Intent editIntent = getIntent();
        weapon = (WeaponData) editIntent.getSerializableExtra("weapon");
        //System.out.println("Weapon name: " + weapon.name);
        //System.out.println("Weapon id: " + weapon.id);
        attachmentSpinnerList = new ArrayList<>();

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
        if (weapon.attachment != null){
            attachment.setSelection(manager.getAttachmentPosition(weapon.attachment) + 1);
        }
        manager.registerAttachmentSpinnerView(this, this);
        updateAttachmentSpinnerList();

        //Assign current values:
        name.setText(weapon.name);
        damage.setText(Integer.toString(weapon.damage));
        firstTalent.setSelection(StaticLists.getTalentPosition(weapon.firstTalent));
        secondTalent.setSelection(StaticLists.getTalentPosition(weapon.secondTalent));
        freeTalent.setSelection(StaticLists.getTalentPosition(weapon.freeTalent));
    }

    @Override
    public void onRestart(){
        updateAttachmentSpinnerList();
        super.onRestart();
    }

    public void updateWeapon(View v){
        weapon.name = name.getText().toString();
        weapon.damage = Integer.parseInt(damage.getText().toString());
        weapon.firstTalent = firstTalent.getSelectedItem().toString();
        weapon.secondTalent = secondTalent.getSelectedItem().toString();
        weapon.freeTalent = freeTalent.getSelectedItem().toString();
        int selectedAttach = attachment.getSelectedItemPosition();
        if (selectedAttach == 0){
            weapon.attachment = null;
        }
        else{
            weapon.attachment = manager.getAttachmentId(selectedAttach - 1);
        }
        manager.updateWeapon(weapon);
        onBackPressed();
    }

    public void deleteWeapon(View v){
        manager.deleteWeapon(weapon);
        onBackPressed();
    }

    @Override
    synchronized public void updateAttachmentSpinnerList(){
        attachmentSpinnerList.clear();
        manager.copyAttachmentSpinner(attachmentSpinnerList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, attachmentSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attachment.setAdapter(adapter);
        if (weapon.attachment != null){
            attachment.setSelection(manager.getAttachmentPosition(weapon.attachment) + 1);
        }
    }

    @Override
    public void onDestroy(){
        manager.removeAttachmentSpinnerView(this);
        super.onDestroy();
    }

}
