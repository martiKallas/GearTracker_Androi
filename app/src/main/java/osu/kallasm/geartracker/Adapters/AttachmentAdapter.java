package osu.kallasm.geartracker.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import osu.kallasm.geartracker.DataModels.AttachmentData;
import osu.kallasm.geartracker.EditAttachment;
import osu.kallasm.geartracker.R;
import osu.kallasm.geartracker.Utils.ListManager;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {

    private List<AttachmentData> attachmentList;
    public class AttachmentViewHolder extends RecyclerView.ViewHolder{
        public TextView aList_name, aList_primaryAttribute, aList_primaryValue, aList_secondaryAttribute, aList_secondaryValue, aList_weaponName;
        public Button aList_editAttachment;
        private ListManager manager = ListManager.getListManager(null);

        public AttachmentViewHolder(View view){
            super(view);
            aList_name = (TextView) view.findViewById(R.id.aList_name);
            aList_primaryAttribute = (TextView) view.findViewById(R.id.aList_primaryAttribute);
            aList_primaryValue = (TextView) view.findViewById(R.id.aList_primaryValue);
            aList_secondaryAttribute = (TextView) view.findViewById(R.id.aList_secondaryAttribute);
            aList_secondaryValue = (TextView) view.findViewById(R.id.aList_secondaryValue);
            aList_editAttachment = (Button) view.findViewById(R.id.aList_editAttachment);
            aList_weaponName = (TextView) view.findViewById(R.id.aList_weaponName);

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
        //From attempt to include spinner in recyclerView
        if (attch.attached_to == null){
            holder.aList_weaponName.setText("No Weapon");
        }
        else{
            int weapPos = holder.manager.getWeaponPosition(attch.attached_to);
            String spinnerDescr = null;
            if (weapPos >= 0) {
                spinnerDescr = holder.manager.getWeaponSpinnerString(weapPos + 1);
            }
            if (spinnerDescr != null) {
                holder.aList_weaponName.setText(spinnerDescr);
            }
            else{
                holder.aList_weaponName.setText("Loading...");
            }
        }
    }

    @Override
    public int getItemCount() {return attachmentList.size();}

}
