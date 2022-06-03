package com.cookandroid.smartmirror.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.MirrorDBHelper;
import com.cookandroid.smartmirror.MirrorNetworkHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.activities.BelongingSetAddActivity;
import com.cookandroid.smartmirror.dataClass.belongingSetData;

import java.util.ArrayList;

public class BelongingSetRecyclerAdapter extends RecyclerView.Adapter<BelongingSetRecyclerAdapter.ViewHolder> {
    ArrayList<belongingSetData> belongingSetDataArrayList;
    ArrayList<Switch> belongingSetSwitchArrayList;
    ActivityResultLauncher<Intent> mStartForResult;
    Context fragmentContext;
    MirrorDBHelper sqlDB;
    MirrorNetworkHelper networkHelper;
    public void setFragmentContext(Context context){fragmentContext=context;}
    public void setmStartForResult(ActivityResultLauncher<Intent> activityResultLauncher){mStartForResult = activityResultLauncher;}
    public void addItem(belongingSetData item){
        String belonging_id = networkHelper.addBelongingSetToServer(item);
        try{
            item.setBelonging_id(Integer.parseInt(belonging_id));
            sqlDB.addBelongingSet(item);
            belongingSetDataArrayList.add(item);
            notifyItemInserted(belongingSetDataArrayList.size());
        }catch (NumberFormatException e){
            e.printStackTrace();
            Log.i("BelongingSetRecyclerAdapter", "소지품세트 추가 실패");
        }
    }
    public void editItem(int index, belongingSetData changeItem){
        if(networkHelper.editBelongingSetToServer(changeItem)){
            sqlDB.editBelongingSet(changeItem);
            belongingSetDataArrayList.set(index, changeItem);
            notifyItemChanged(index);
        }else{
            Log.i("BelongingSetRecyclerAdapter", "소지품세트 수정 실패");
        }
    }
    public void removeAt(int index){
        if(networkHelper.delBelongingSetToServer(belongingSetDataArrayList.get(index))){
            sqlDB.delBelongingSet(belongingSetDataArrayList.get(index));
            belongingSetDataArrayList.remove(index);
            notifyItemRemoved(index);
        }else{
            Log.i("BelongingSetRecyclerAdapter", "소지품세트 제거 실패");
        }
    }
    public void setSwitchAt(int index, boolean isActivted){
        Log.i("setSwitchAt", index+"번째 스위치를 "+isActivted+"한 상태로");
        if(isActivted){
            // 활성화 상태로 바꿀 시 나머지를 비활성화 시켜야 한다.
            if(networkHelper.activationBelongingSetToServer(belongingSetDataArrayList.get(index))){
                belongingSetDataArrayList.get(index).setActiavted(isActivted);
                belongingSetSwitchArrayList.get(index).setChecked(isActivted);
                sqlDB.setBelongingSetActiavted(belongingSetDataArrayList.get(index));
            }else{
                Log.i("BelongingSetRecyclerAdapter", "소지품세트 활성화 실패");
            }
        }else{
            // 비활성화 상태로 바꿀 시 하나만 비활성화 시키면 된다.
            if(networkHelper.deactivationBelongingSetToServer(belongingSetDataArrayList.get(index))){
                belongingSetDataArrayList.get(index).setActiavted(isActivted);
                belongingSetSwitchArrayList.get(index).setChecked(isActivted);
                sqlDB.setBelongingSetActiavted(belongingSetDataArrayList.get(index));
            }else{
                Log.i("BelongingSetRecyclerAdapter", "소지품세트 비활성화 실패");
            }
        }

    }
    public BelongingSetRecyclerAdapter(ArrayList<belongingSetData> dataList, MirrorDBHelper sqlDB){
        belongingSetDataArrayList = dataList;
        belongingSetSwitchArrayList = new ArrayList<Switch>();
        this.sqlDB = sqlDB;
        this.networkHelper = sqlDB.getNetworkHelper();
    }

    @NonNull
    @Override
    public BelongingSetRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_belonging_set, parent, false);
        BelongingSetRecyclerAdapter.ViewHolder viewHolder = new BelongingSetRecyclerAdapter.ViewHolder(view);
        return viewHolder;
    }
    protected int getCheckedIndex(){
        // 소지품세트 리스트를 돌면서 스위치가 체크된 게 있다면 그 인덱스를 반환하고 없다면 -1를 반환합니다.
        for(int i=0; i< belongingSetDataArrayList.size(); i++){
            if(belongingSetDataArrayList.get(i).isActiavted()) return i;

        }
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull BelongingSetRecyclerAdapter.ViewHolder holder, int position) {
        belongingSetData item = belongingSetDataArrayList.get(position);
        holder.data = item;
        holder.set_name.setText(item.getSet_name());
        holder.set_info.setText(item.getSet_info());
        holder.belongingSwitch.setChecked(item.isActiavted());
        holder.belongingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // isChecked: 변경된 체크
                if(!isChecked){
                    // 해제될 때
                    // 해당 인덱스가 해제되었다는 정보만 저장
                    setSwitchAt(holder.getAdapterPosition(),false);
                }else{
                    // 체크될 때
                    // 다른 스위치를 확인해서 체크된 게 있다면 그 스위치를 해제시켜야 함.
                    int checkedIndex = getCheckedIndex();
                    if(checkedIndex == -1){
                        // 리턴값이 -1이면 체크된 게 없으므로 걍 스위치를 체크함
//                        Log.i("belongingSetRecyclerAdapter", "체크되어있는 스위치가 없습니다.");
                        setSwitchAt(holder.getAdapterPosition(),true);
                    }else{
                        // 리턴값이 있다면 해당 인덱스의 스위치를 off해준다.
//                        Log.i("belongingSetRecyclerAdapter", "체크되어있던 스위치 인덱스: "+checkedIndex);
                        setSwitchAt(checkedIndex,false);
                    }

                    // 현재 스위치를 체크된 상태로
                    setSwitchAt(holder.getAdapterPosition(),true);
                }
            }
        });
        belongingSetSwitchArrayList.add(holder.getAdapterPosition(),holder.belongingSwitch);

        holder.mainLiayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 소지품 프리셋 추가 액티비티로 전환 - 수정임
                // 전달되는 데이터 O
//                Log.i("belongingSetRecyclerAdapter", "소지품세트를 수정합니다.");
                Intent intent = new Intent(fragmentContext, BelongingSetAddActivity.class);
                intent.putExtra("isAdd", false);
                intent.putExtra("index", holder.getAdapterPosition());
                intent.putExtra("belongingSet", holder.data);
                mStartForResult.launch(intent);
            }
        });
        holder.belongingSetDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("belongingSetRecyclerAdapter", "소지품세트를 삭제합니다.");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.create();
                alertDialog
                        .setTitle("정말 삭제 하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Log.i("belongingSetDelDialog", holder.getAdapterPosition()+"번째 아이템 "+"삭제");
                                removeAt(holder.getAdapterPosition());
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Log.i("belongingSetDelDialog", "취소");
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return belongingSetDataArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        // 소지품세트
        private TextView set_name, set_info;
        private Switch belongingSwitch;
        private ImageView belongingSetDelBtn;
        private LinearLayout mainLiayout;
        private belongingSetData data;
        private Context itemContext;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemContext = itemView.getContext();
            set_name = (TextView) itemView.findViewById(R.id.belongingSetItemName);
            set_info = (TextView) itemView.findViewById(R.id.belongingSetItemInfo);
            belongingSwitch = (Switch) itemView.findViewById(R.id.belongingSetItemSwitch);
            mainLiayout = (LinearLayout) itemView.findViewById(R.id.belongingPresetItemLayout);
            belongingSetDelBtn = (ImageView) itemView.findViewById(R.id.belongingSetDelBtn);

        }
    }
}
