package com.cookandroid.smartmirror.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.activities.BelongingSetAddActivity;
import com.cookandroid.smartmirror.dataClass.belongingSetData;

import java.util.ArrayList;

public class BelongingSetRecyclerAdapter extends RecyclerView.Adapter<BelongingSetRecyclerAdapter.ViewHolder> {
    ArrayList<belongingSetData> belongingSetDataArrayList;
    ArrayList<Switch> belongingSetSwitchArrayList;
    ActivityResultLauncher<Intent> mStartForResult;
    Context fragmentContext;
    public void setFragmentContext(Context context){fragmentContext=context;}
    public void setmStartForResult(ActivityResultLauncher<Intent> activityResultLauncher){mStartForResult = activityResultLauncher;}
    public void addItem(belongingSetData item){
        belongingSetDataArrayList.add(item);
        notifyItemInserted(belongingSetDataArrayList.size());
        notifyItemRangeChanged(belongingSetDataArrayList.size()-1, belongingSetDataArrayList.size());
        notifyDataSetChanged();
    }
    public void editItem(int index, belongingSetData changeItem){
        belongingSetDataArrayList.set(index, changeItem);
        notifyItemChanged(index);
        notifyDataSetChanged();
    }
    public void removeAt(int index){
        belongingSetDataArrayList.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, belongingSetDataArrayList.size());
    }
    public BelongingSetRecyclerAdapter(ArrayList<belongingSetData> dataList){
        belongingSetDataArrayList = dataList;
        belongingSetSwitchArrayList = new ArrayList<Switch>();
    }
    @NonNull
    @Override
    public BelongingSetRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.belonging_set_item, parent, false);
        BelongingSetRecyclerAdapter.ViewHolder viewHolder = new BelongingSetRecyclerAdapter.ViewHolder(view);
        return viewHolder;
    }
    protected int getCheckedIndex(){
        // 소지품세트 리스트를 돌면서 스위치가 체크된 게 있다면 그 인덱스를 반환하고 없다면 -1를 반환합니다.
        for(int i=0; i< belongingSetDataArrayList.size(); i++){
            if(belongingSetDataArrayList.get(i).getSelected()) return i;

        }
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull BelongingSetRecyclerAdapter.ViewHolder holder, int position) {
        belongingSetData item = belongingSetDataArrayList.get(position);
        holder.data = item;
        holder.name.setText(item.getName());
        holder.detail.setText(item.getDetail());
        holder.belongingSwitch.setChecked(item.getSelected());
        holder.belongingSwitch.setText("활성화");
        holder.belongingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // isChecked: 변경된 체크
                if(!isChecked){
                    // 해제될 때
                    // 해당 인덱스가 해제되었다는 정보만 저장
                    belongingSetDataArrayList.get(holder.getAdapterPosition()).setSelected(false);

                }else{
                    // 체크될 때
                    // 다른 스위치를 확인해서 체크된 게 있다면 그 스위치를 해제시켜야 함.
                    int index = getCheckedIndex();
                    if(index == -1){
                        // 리턴값이 -1이면 체크된 게 없으므로 걍 스위치를 체크함
                        Log.i("belongingSetRecyclerAdapter", "체크되어있는 스위치가 없습니다.");
                    }else{
                        // 리턴값이 있다면 해당 인덱스의 스위치를 off해준다.
                        Log.i("belongingSetRecyclerAdapter", "체크되어있던 스위치 인덱스: "+index);

                        belongingSetDataArrayList.get(index).setSelected(false);
                        belongingSetSwitchArrayList.get(index).setChecked(false);
                    }

                    // 현재 스위치를 체크된 상태로
                    belongingSetDataArrayList.get(holder.getAdapterPosition()).setSelected(true);
                }
            }
        });
        belongingSetSwitchArrayList.add(holder.getAdapterPosition(),holder.belongingSwitch);

        holder.mainLiayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 소지품 프리셋 추가 액티비티로 전환 - 수정임
                // 전달되는 데이터 O
                Log.i("belongingSetRecyclerAdapter", "소지품세트를 수정합니다.");
                Intent intent = new Intent(fragmentContext, BelongingSetAddActivity.class);
                intent.putExtra("isAdd", false);
                intent.putExtra("index", holder.getAdapterPosition());
                intent.putExtra("belongingSet", holder.data);
                mStartForResult.launch(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return belongingSetDataArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        // 소지품세트
        private TextView name, detail;
        private Switch belongingSwitch;
        private LinearLayout mainLiayout;
        private belongingSetData data;
        private Context itemContext;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemContext = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.belongingPresetItemName);
            detail = (TextView) itemView.findViewById(R.id.belongingPresetItemDetail);
            belongingSwitch = (Switch) itemView.findViewById(R.id.belongingPresetItemSwitch);
            mainLiayout = (LinearLayout) itemView.findViewById(R.id.belongingPresetItemLayout);

        }
    }
}
