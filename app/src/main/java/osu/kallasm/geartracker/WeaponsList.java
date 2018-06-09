//Source: https://www.androidhive.info/2016/01/android-working-with-recycler-view/
package osu.kallasm.geartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import osu.kallasm.geartracker.Adapters.WeaponAdapter;
import osu.kallasm.geartracker.DataModels.WeaponData;
import osu.kallasm.geartracker.Interfaces.WeaponListView;
import osu.kallasm.geartracker.Utils.ListManager;

public class WeaponsList extends AppCompatActivity implements WeaponListView {
    private WeaponAdapter adapter;
    private RecyclerView recyclerView;
    private ListManager manager;
    private ArrayList<WeaponData> weaponList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapons_list);

        recyclerView = (RecyclerView) findViewById(R.id.wList_recyclerView);
        RecyclerView.LayoutManager gLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(gLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(adapter);

       manager = ListManager.getListManager(null);
       weaponList = new ArrayList<>();
       manager.copyWeapons(weaponList);
       if(weaponList.size() > 0){
           adapter = new WeaponAdapter(weaponList);
           updateWeaponList(weaponList);
       }
       manager.registerWeaponListView(this);
    }

    @Override
    public void onBackPressed(){
        manager.registerWeaponListView(this);
        super.onBackPressed();
    }

    @Override
    synchronized public void updateWeaponList(List<WeaponData> list){
        adapter = new WeaponAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    public void showAddWeapon(View v){
        Intent intent = new Intent(WeaponsList.this, AddWeapon.class);
        startActivity(intent);
    }

    public void addAttachment(View v){
    }
}
