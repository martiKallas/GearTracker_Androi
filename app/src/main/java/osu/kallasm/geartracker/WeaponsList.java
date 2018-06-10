//Source: https://www.androidhive.info/2016/01/android-working-with-recycler-view/
package osu.kallasm.geartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import osu.kallasm.geartracker.Adapters.AttachmentAdapter;
import osu.kallasm.geartracker.Adapters.WeaponAdapter;
import osu.kallasm.geartracker.DataModels.WeaponData;
import osu.kallasm.geartracker.Interfaces.AttachmentSpinnerView;
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


        manager = ListManager.getListManager(null);
        weaponList = new ArrayList<>();
        manager.registerWeaponListView(this, this);
        updateWeaponList();
        //source: https://stackoverflow.com/questions/30397460/how-to-know-when-the-recyclerview-has-finished-laying-down-the-items
        //source2: https://stackoverflow.com/questions/37116048/android-global-layout-listener-called-repeatedly-in-android
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refreshRecycler();
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onRestart(){
        updateWeaponList();
        super.onRestart();
    }

    @Override
    public void onDestroy(){
        manager.removeWeaponListView(this);
        super.onDestroy();
    }

    private void refreshRecycler(){
        if(weaponList.size() >= 0) {
            adapter = new WeaponAdapter(weaponList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateWeaponList(){
        System.out.println("Updating weapon list");
        weaponList.clear();
        manager.copyWeapons(weaponList);
        refreshRecycler();
    }

    public void showAddWeapon(View v){
        Intent intent = new Intent(WeaponsList.this, AddWeapon.class);
        startActivity(intent);
    }

}
