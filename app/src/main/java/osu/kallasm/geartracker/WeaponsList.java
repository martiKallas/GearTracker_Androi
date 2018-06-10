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

public class WeaponsList extends AppCompatActivity implements WeaponListView, AttachmentSpinnerView {
    private WeaponAdapter adapter;
    private RecyclerView recyclerView;
    private ListManager manager;
    private ArrayList<WeaponData> weaponList;
    private ArrayList<String> attachmentSpinnerList;

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
        attachmentSpinnerList = new ArrayList<>();
        manager.registerWeaponListView(this);
        updateWeaponList();
        manager.registerAttachmentSpinnerView(this);
        //source: https://stackoverflow.com/questions/30397460/how-to-know-when-the-recyclerview-has-finished-laying-down-the-items
        //source2: https://stackoverflow.com/questions/37116048/android-global-layout-listener-called-repeatedly-in-android
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updateAttachmentSpinnerList();
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onBackPressed(){
        manager.removeWeaponListView(this);
        manager.removeAttachmentSpinnerView(this);
        super.onBackPressed();
    }

    @Override
    public synchronized void updateWeaponList(){
        weaponList.clear();
        manager.copyWeapons(weaponList);
        if(weaponList.size() >= 0) {
            adapter = new WeaponAdapter(weaponList);
            recyclerView.setAdapter(adapter);
        }
    }

    public void showAddWeapon(View v){
        Intent intent = new Intent(WeaponsList.this, AddWeapon.class);
        startActivity(intent);
    }

    public void addAttachment(View v){
        //get recycler position

        //get spinner position
        //get attachment at spinner position
        //send update notification
        //hide attachment info
        //manager should update Recycler on success or failure
    }

    @Override
    public synchronized void updateAttachmentSpinnerList(){
        attachmentSpinnerList.clear();
        manager.copyAttachmentSpinner(attachmentSpinnerList);
        System.out.println("In update attachment with list size = " + attachmentSpinnerList.size());
        System.out.println("Recycler view children: " + recyclerView.getChildCount());
        //Source: https://stackoverflow.com/questions/32811156/how-to-iterate-over-recyclerview-items
        for(int i = 0; i < recyclerView.getChildCount(); i++){
            WeaponAdapter.WeaponViewHolder holder = (WeaponAdapter.WeaponViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (holder == null) System.out.println("Holder is null at i = " + i);
            else{
                System.out.println("Holder is not null");
                holder.setSpinner(this, attachmentSpinnerList);
            }
        }
    }
}
