//Source: https://www.androidhive.info/2016/01/android-working-with-recycler-view/
package osu.kallasm.geartracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import osu.kallasm.geartracker.Adapters.WeaponAdapter;
import osu.kallasm.geartracker.DataModels.WeaponData;

public class WeaponsList extends AppCompatActivity {
    private WeaponAdapter adapter;
    private RecyclerView recyclerView;
    private List<WeaponData> weaponList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapons_list);

        recyclerView = (RecyclerView) findViewById(R.id.wList_recyclerView);
        adapter = new WeaponAdapter(weaponList);
        RecyclerView.LayoutManager gLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(gLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(adapter);

        HttpHandler http = new HttpHandler();
        try {
            http.getWeapons(this);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void updateList(List<WeaponData> list){
        adapter = new WeaponAdapter(list);
        recyclerView.setAdapter(adapter);
    }
}
