package osu.kallasm.geartracker;

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

public class AddAttachment extends AppCompatActivity {

    Spinner primaryAttribute, secondaryAttribute;
    EditText name, primaryValue, secondaryValue;
    ListManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attachment);

        manager = ListManager.getListManager(null);

        //find views
        primaryAttribute = (Spinner)findViewById(R.id.addAttachment_primaryAttribute);
        secondaryAttribute = (Spinner)findViewById(R.id.addAttachment_secondaryAttribute);
        secondaryValue = (EditText)findViewById(R.id.addAttachment_secondaryValue);
        name = (EditText)findViewById(R.id.addAttachment_name);
        primaryValue = (EditText)findViewById(R.id.addAttachment_primaryValue);

        //assign spinner values
        String[] content = StaticLists.ATTRIBUTES;
        //Source: https://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, content);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        primaryAttribute.setAdapter(adapter);
        secondaryAttribute.setAdapter(adapter);

        //set filter on primaryValue input
        primaryValue.setFilters(new InputFilter[]{new PercentFilter(0, 100)});
    }

    public void addAttachment(View v){
        AttachmentData newAttachment = new AttachmentData();
        newAttachment.name = name.getText().toString();
        newAttachment.primaryValue = Integer.parseInt(primaryValue.getText().toString());
        newAttachment.primaryAttribute = primaryAttribute.getSelectedItem().toString();
        newAttachment.secondaryAttribute = secondaryAttribute.getSelectedItem().toString();
        newAttachment.secondaryValue = Integer.parseInt(secondaryValue.getText().toString());
        newAttachment.attached_to = null;
        manager.addAttachment(newAttachment);
    }
}
