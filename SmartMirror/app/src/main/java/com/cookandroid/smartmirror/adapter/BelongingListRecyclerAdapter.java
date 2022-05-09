package com.cookandroid.smartmirror.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.belongingSetData;

import java.util.ArrayList;

public class BelongingListRecyclerAdapter extends RecyclerView.Adapter<BelongingListRecyclerAdapter.ViewHolder>{
    ArrayList<belongingSetData> belongingSetDataArrayList;
    public void addItem(belongingSetData item){
        belongingSetDataArrayList.add(item);
    }
    public void editItem(int index, belongingSetData changeItem){
        belongingSetDataArrayList.set(index, changeItem);
    }
    public void removeAt(int index){
        belongingSetDataArrayList.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, belongingSetDataArrayList.size());
    }
    public BelongingListRecyclerAdapter(ArrayList<belongingSetData> dataList){
        belongingSetDataArrayList = dataList;
    }
    @NonNull
    @Override
    public BelongingListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.belonging_set_item, parent, false);
        BelongingListRecyclerAdapter.ViewHolder viewHolder = new BelongingListRecyclerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BelongingListRecyclerAdapter.ViewHolder holder, int position) {
        belongingSetData item = belongingSetDataArrayList.get(position);
        holder.data = item;
        holder.name.setText(item.getName());
        holder.detail.setText(item.getDetail());

    }

    @Override
    public int getItemCount() {
        return belongingSetDataArrayList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name, detail;
        private Switch belongingSwitch;
        belongingSetData data;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.belongingPresetItemName);
            detail = (TextView) itemView.findViewById(R.id.belongingPresetItemDetail);
            belongingSwitch = (Switch) itemView.findViewById(R.id.belongingPresetItemSwitch);

        }
    }
}
