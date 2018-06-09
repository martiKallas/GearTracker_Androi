package osu.kallasm.geartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import osu.kallasm.geartracker.DataModels.AttachmentData;
import osu.kallasm.geartracker.Utils.ListManager;
import osu.kallasm.geartracker.Utils.PercentFilter;
import osu.kallasm.geartracker.Utils.StaticLists;

public class EditAttachment extends AppCompatActivity {

    Spinner primaryAttribute, secondaryAttribute, attached_to;
    EditText name, primaryValue, secondaryValue;
    AttachmentData attachment;
    ListManager manager = ListManager.getListManager(null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attachment);
        Intent editIntent = getIntent();
        attachment = (AttachmentData) editIntent.getSerializableExtra("attachment");
        System.out.println("Attachment name: " + attachment.name);
        System.out.println("Attachment id: " + attachment.id);

        String[] content = StaticLists.ATTRIBUTES;
        //Build the spinners and assign variables
        //Source: https://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, content);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        primaryAttribute = (Spinner)findViewById(R.id.editAttachment_primaryAttribute);
        primaryAttribute.setAdapter(adapter);
        secondaryAttribute = (Spinner)findViewById(R.id.editAttachment_secondaryAttribute);
        secondaryAttribute.setAdapter(adapter);

        name = (EditText)findViewById(R.id.editAttachment_name);
        primaryValue = (EditText)findViewById(R.id.editAttachment_primaryValue);
        primaryValue.setFilters(new InputFilter[]{new PercentFilter(0, 100)});
        secondaryValue = (EditText)findViewById(R.id.editAttachment_secondaryValue);
        secondaryValue.setFilters(new InputFilter[]{new PercentFilter(0, 100)});

        //Set attached_to spinner
        attached_to = (Spinner)findViewById(R.id.editAttachment_attachedTo);

        //Assign current values:
        name.setText(attachment.name);
        primaryValue.setText(Integer.toString(attachment.primaryValue));
        primaryAttribute.setSelection(StaticLists.getAttributePosition(attachment.primaryAttribute));
        secondaryValue.setText(Integer.toString(attachment.secondaryValue));
        secondaryAttribute.setSelection(StaticLists.getTalentPosition(attachment.secondaryAttribute));
    }

    public void updateAttachment(View v){
        attachment.name = name.getText().toString();
        attachment.primaryValue = Integer.parseInt(primaryValue.getText().toString());
        attachment.primaryAttribute = primaryAttribute.getSelectedItem().toString();
        attachment.secondaryAttribute = secondaryAttribute.getSelectedItem().toString();
        attachment.secondaryValue = Integer.parseInt(secondaryValue.getText().toString());
        attachment.attached_to = null;
        manager.updateAttachment(attachment);
    }

    public void deleteAttachment(View v){
        manager.deleteAttachment(attachment);
    }
}
