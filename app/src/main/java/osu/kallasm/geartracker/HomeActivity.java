package osu.kallasm.geartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

import osu.kallasm.geartracker.Utils.ListManager;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ListManager manager = ListManager.getListManager();
        try {
            manager.getWeapons();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void showWeapons(View v){
        Intent intent = new Intent(HomeActivity.this, WeaponsList.class);
        startActivity(intent);
    }

    public void showAttachments(View v){
        Intent intent = new Intent(HomeActivity.this, AttachmentsList.class);
        startActivity(intent);
    }
}
