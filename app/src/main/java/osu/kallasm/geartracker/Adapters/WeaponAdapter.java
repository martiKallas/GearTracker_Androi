//Source: https://www.androidhive.info/2016/01/android-working-with-recycler-view/
package osu.kallasm.geartracker.Adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import osu.kallasm.geartracker.DataModels.WeaponData;
import osu.kallasm.geartracker.R;

public class WeaponAdapter extends RecyclerView.Adapter<WeaponAdapter.WeaponViewHolder> {

    private List<WeaponData> weaponList;
    public class WeaponViewHolder extends RecyclerView.ViewHolder{
        public TextView wList_name, wList_damage, wList_talentOne, wList_talentTwo, wList_freeTalent;
        public WeaponViewHolder(View view){
            super(view);
            wList_name = (TextView) view.findViewById(R.id.wList_name);
            wList_damage = (TextView) view.findViewById(R.id.wList_damage);
            wList_talentOne = (TextView) view.findViewById(R.id.wList_talentOne);
            wList_talentTwo = (TextView) view.findViewById(R.id.wList_talentTwo);
            wList_freeTalent = (TextView) view.findViewById(R.id.wList_freeTalent);
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
        WeaponData wpn = weaponList.get(position);
        holder.wList_name.setText(wpn.name);
        holder.wList_damage.setText(String.valueOf(wpn.damage));
        holder.wList_talentOne.setText(wpn.firstTalent);
        holder.wList_talentTwo.setText(wpn.secondTalent);
        holder.wList_freeTalent.setText(wpn.freeTalent);
        if (wpn.attachment == null){
            //Hide attachment details
            ConstraintLayout attach = holder.itemView.findViewById(R.id.wList_attached);
            //Source: https://stackoverflow.com/questions/5756136/how-to-hide-a-view-programmatically
            attach.setVisibility(View.GONE);
        }
        else{
            ConstraintLayout unattach = holder.itemView.findViewById(R.id.wList_unattachedLayout);
            unattach.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {return weaponList.size();}

}
