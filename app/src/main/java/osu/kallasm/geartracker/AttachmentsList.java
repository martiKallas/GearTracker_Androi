package osu.kallasm.geartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

import osu.kallasm.geartracker.Adapters.AttachmentAdapter;
import osu.kallasm.geartracker.Adapters.WeaponAdapter;
import osu.kallasm.geartracker.DataModels.AttachmentData;
import osu.kallasm.geartracker.Interfaces.AttachmentListView;
import osu.kallasm.geartracker.Interfaces.WeaponSpinnerView;
import osu.kallasm.geartracker.Utils.ListManager;

public class AttachmentsList extends AppCompatActivity implements AttachmentListView, WeaponSpinnerView {
    private AttachmentAdapter adapter;
    private RecyclerView recyclerView;
    private ListManager manager;
    private ArrayList<AttachmentData> attachmentList;
    private ArrayList<String> weaponSpinnerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachments_list);

        recyclerView = (RecyclerView) findViewById(R.id.aList_recyclerView);
        RecyclerView.LayoutManager gLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(gLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        manager = ListManager.getListManager(null);
        attachmentList = new ArrayList<>();
        manager.registerAttachmentListView(this);
        updateAttachmentList();

        weaponSpinnerList = new ArrayList<>();
        manager.registerWeaponSpinnerView(this);
        //source: https://stackoverflow.com/questions/30397460/how-to-know-when-the-recyclerview-has-finished-laying-down-the-items
        //source2: https://stackoverflow.com/questions/37116048/android-global-layout-listener-called-repeatedly-in-android
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updateWeaponSpinnerList();
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onBackPressed(){
        manager.removeAttachmentListView(this);
        manager.removeWeaponSpinnerView(this);
        super.onBackPressed();
    }

    @Override
    public synchronized void updateAttachmentList(){
        attachmentList.clear();
        manager.copyAttachments(attachmentList);
        if (attachmentList.size() >= 0) {
            adapter = new AttachmentAdapter(attachmentList);
            recyclerView.setAdapter(adapter);
        }
    }

    public void showAddAttachment(View v){
        Intent intent = new Intent(AttachmentsList.this, AddAttachment.class);
        startActivity(intent);
    }

    public void addAttachment(View v){
    }

    @Override
    public synchronized void updateWeaponSpinnerList(){
        weaponSpinnerList.clear();
        manager.copyWeaponSpinner(weaponSpinnerList);
        //Source: https://stackoverflow.com/questions/32811156/how-to-iterate-over-recyclerview-items
        for(int i = 0; i < recyclerView.getChildCount(); i++){
            AttachmentAdapter.AttachmentViewHolder holder = (AttachmentAdapter.AttachmentViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (holder == null) System.out.println("Holder is null at i = " + i);
            else{
                holder.setSpinner(this, weaponSpinnerList);
            }
        }
    }
}
