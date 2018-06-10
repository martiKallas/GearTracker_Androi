//Source: https://www.androidhive.info/2016/01/android-working-with-recycler-view/
package osu.kallasm.geartracker.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import osu.kallasm.geartracker.DataModels.WeaponData;
import osu.kallasm.geartracker.EditWeapon;
import osu.kallasm.geartracker.R;
import osu.kallasm.geartracker.Utils.ListManager;

public class WeaponAdapter extends RecyclerView.Adapter<WeaponAdapter.WeaponViewHolder> {

    private List<WeaponData> weaponList;
    public class WeaponViewHolder extends RecyclerView.ViewHolder{
        public TextView wList_name, wList_damage, wList_talentOne, wList_talentTwo, wList_freeTalent, wList_attachmentName;
        public Button wList_editWeapon;
        private ListManager manager = ListManager.getListManager(null);

        public WeaponViewHolder(View view){
            super(view);
            wList_name = (TextView) view.findViewById(R.id.wList_name);
            wList_damage = (TextView) view.findViewById(R.id.wList_damage);
            wList_talentOne = (TextView) view.findViewById(R.id.wList_talentOne);
            wList_talentTwo = (TextView) view.findViewById(R.id.wList_talentTwo);
            wList_freeTalent = (TextView) view.findViewById(R.id.wList_freeTalent);
            wList_attachmentName = (TextView) view.findViewById(R.id.wList_attachmentName);
            wList_editWeapon = (Button) view.findViewById(R.id.wList_editWeapon);

            if(wList_editWeapon != null) {
                wList_editWeapon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        WeaponData weapon = weaponList.get(position);
                        System.out.println("Edit weapon " + weapon.name);
                        //Get Activity source: https://stackoverflow.com/questions/8276634/android-get-hosting-activity-from-a-view
                        Activity wList = (Activity) v.getContext();
                        Intent editIntent = new Intent(wList, EditWeapon.class);
                        //Passing object source: https://stackoverflow.com/questions/3323074/android-difference-between-parcelable-and-serializable
                        editIntent.putExtra("weapon", weapon);
                        wList.startActivity(editIntent);
                    }
                });
            }
        }
    }

    public void setWeaponList(List<WeaponData> list){
        this.weaponList = list;
    }

    public WeaponAdapter(List<WeaponData> weaponList) { this.weaponList = weaponList;}

    @Override
    public WeaponViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weapons_row, parent, false);
        return new WeaponViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WeaponViewHolder holder, int position){
        if(weaponList.size() <= 0) return;
        WeaponData wpn = weaponList.get(position);
        holder.wList_name.setText(wpn.name);
        holder.wList_damage.setText(String.valueOf(wpn.damage));
        holder.wList_talentOne.setText(wpn.firstTalent);
        holder.wList_talentTwo.setText(wpn.secondTalent);
        holder.wList_freeTalent.setText(wpn.freeTalent);
        if (wpn.attachment == null) {
            holder.wList_attachmentName.setText("No attachment");
        }
        else{
            int attachPos = holder.manager.getAttachmentPosition(wpn.attachment);
            String attachDescr = null;
            if (attachPos >= 0) {
                attachDescr = holder.manager.getAttachmentSpinnerString(attachPos + 1);
            }
            if (attachDescr != null){
                holder.wList_attachmentName.setText(attachDescr);
            }
            else{
                holder.wList_attachmentName.setText("Loading...");
            }
        }

    }

    @Override
    public int getItemCount() {return weaponList.size();}

}
