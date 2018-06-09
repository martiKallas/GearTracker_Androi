package osu.kallasm.geartracker.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import osu.kallasm.geartracker.DataModels.AttachmentData;
import osu.kallasm.geartracker.EditAttachment;
import osu.kallasm.geartracker.R;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {

    private List<AttachmentData> attachmentList;
    public class AttachmentViewHolder extends RecyclerView.ViewHolder{
        public TextView aList_name, aList_primaryAttribute, aList_primaryValue, aList_secondaryAttribute, aList_secondaryValue;
        public Button aList_unattach, aList_attach, aList_editAttachment;
        public AttachmentViewHolder(View view){
            super(view);
            aList_name = (TextView) view.findViewById(R.id.aList_name);
            aList_primaryAttribute = (TextView) view.findViewById(R.id.aList_primaryAttribute);
            aList_primaryValue = (TextView) view.findViewById(R.id.aList_primaryValue);
            aList_secondaryAttribute = (TextView) view.findViewById(R.id.aList_secondaryAttribute);
            aList_secondaryValue = (TextView) view.findViewById(R.id.aList_secondaryValue);
            aList_unattach = (Button) view.findViewById(R.id.aList_unattach);
            aList_attach = (Button) view.findViewById(R.id.aList_addAttachment);
            aList_editAttachment = (Button) view.findViewById(R.id.aList_editAttachment);

            if(aList_unattach != null) {
                //source: http://www.jyotman.xyz/post/creating-add-and-remove-type-list-using-recyclerview
                aList_unattach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        AttachmentData attachment = attachmentList.get(position);
                        System.out.println("Remove from weapon: " + attachment.name);
                    }
                });
            }

            if(aList_attach != null) {
                aList_attach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        AttachmentData attachment = attachmentList.get(position);
                        System.out.println("Add to weapon: " + attachment.name);
                    }
                });
            }

            if(aList_editAttachment != null) {
                aList_editAttachment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        AttachmentData attachment = attachmentList.get(position);
                        System.out.println("Edit attachment " + attachment.name);
                        //Get Activity source: https://stackoverflow.com/questions/8276634/android-get-hosting-activity-from-a-view
                        Activity aList = (Activity) v.getContext();
                        Intent editIntent = new Intent(aList, EditAttachment.class);
                        //Passing object source: https://stackoverflow.com/questions/3323074/android-difference-between-parcelable-and-serializable
                        editIntent.putExtra("attachment", attachment);
                        aList.startActivity(editIntent);
                    }
                });
            }
        }
    }

    public void setAttachmentList(List<AttachmentData> list){
        this.attachmentList = list;
    }

    public AttachmentAdapter(List<AttachmentData> attachmentList) { this.attachmentList = attachmentList;}

    @Override
    public AttachmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attachments_row, parent, false);
        return new AttachmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AttachmentViewHolder holder, int position){
        System.out.println("Attachments size " + attachmentList.size());
        if (attachmentList.size() <= 0) return;
        AttachmentData attch = attachmentList.get(position);
        holder.aList_name.setText(attch.name);
        holder.aList_primaryAttribute.setText(attch.primaryAttribute);
        holder.aList_primaryValue.setText(String.valueOf(attch.primaryValue));
        holder.aList_secondaryAttribute.setText(attch.secondaryAttribute);
        holder.aList_secondaryValue.setText(String.valueOf(attch.secondaryValue));
        if (attch.attached_to == null){
            //Hide attachment details
            ConstraintLayout attach = holder.itemView.findViewById(R.id.aList_attached);
            //Source: https://stackoverflow.com/questions/5756136/how-to-hide-a-view-programmatically
            attach.setVisibility(View.GONE);
        }
        else{
            ConstraintLayout unattach = holder.itemView.findViewById(R.id.aList_unattachedLayout);
            unattach.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {return attachmentList.size();}

}
