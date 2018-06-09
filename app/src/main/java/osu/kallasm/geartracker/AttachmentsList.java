package osu.kallasm.geartracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import osu.kallasm.geartracker.Adapters.AttachmentAdapter;
import osu.kallasm.geartracker.DataModels.AttachmentData;
import osu.kallasm.geartracker.Interfaces.AttachmentListView;
import osu.kallasm.geartracker.Utils.ListManager;

public class AttachmentsList extends AppCompatActivity implements AttachmentListView {
    private AttachmentAdapter adapter;
    private RecyclerView recyclerView;
    private ListManager manager;
    private ArrayList<AttachmentData> attachmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachments_list);

        recyclerView = (RecyclerView) findViewById(R.id.aList_recyclerView);
        RecyclerView.LayoutManager gLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(gLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(adapter);

        manager = ListManager.getListManager(null);
        attachmentList = new ArrayList<>();
        manager.copyAttachments(attachmentList);
        if(attachmentList.size() > 0){
            adapter = new AttachmentAdapter(attachmentList);
            updateAttachmentList(attachmentList);
        }
        manager.registerAttachmentListView(this);
    }

    @Override
    public void onBackPressed(){
        manager.registerAttachmentListView(this);
        super.onBackPressed();
    }

    @Override
    synchronized public void updateAttachmentList(List<AttachmentData> list){
        adapter = new AttachmentAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    public void showAddAttachment(View v){
        Intent intent = new Intent(AttachmentsList.this, AddAttachment.class);
        startActivity(intent);
    }

    public void addAttachment(View v){
    }
}
