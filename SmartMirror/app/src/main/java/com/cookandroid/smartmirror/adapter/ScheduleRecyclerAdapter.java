package com.cookandroid.smartmirror.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.MirrorDBHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.custom.ScheduleTypeDialog;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.scheduleData;
import com.cookandroid.smartmirror.dataClass.userData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder> {
    ArrayList<scheduleData> mData = new ArrayList<>();
    FragmentManager fm;
    View.OnClickListener onClickListener;
    userData selectedUser;
    MirrorDBHelper sqlDB;
    MyApplication myApp;
    static int[] iconResArray = {R.drawable.ic_schedule_black, R.drawable.ic_schedule_blue, R.drawable.ic_schedule_green, R.drawable.ic_schedule_orange, R.drawable.ic_schedule_purple};
    public void sortItems(){
        Collections.sort(mData, new Comparator<scheduleData>() {
            @Override
            public int compare(scheduleData s1, scheduleData s2) {
                return s1.getStartTime().compareTo(s2.getStartTime());
            }
        });
    }
    public ScheduleRecyclerAdapter(ArrayList<scheduleData> mList, MirrorDBHelper sqlDB, MyApplication myApp){
        mData = mList;
        this.selectedUser = myApp.getSelectedUser();
        this.sqlDB = sqlDB;
        this.myApp = myApp;
    }
    public void addItem(scheduleData sData){
        sqlDB.addSchedule(sData);
        mData.add(sData);
        sortItems();
        notifyDataSetChanged();
//        notifyItemInserted(mData.size()-1);
    }
    public void editItem(int index, scheduleData data){
        sqlDB.editSchedule(data);
        mData.set(index, data);
        sortItems();
        notifyDataSetChanged();
        //        notifyItemChanged(index);
    }
    public void removeAt(int index){
        sqlDB.delSchedule(mData.get(index));
        mData.remove(index);
//        notifyItemRemoved(index);
        sortItems();
        notifyDataSetChanged();

    }
    public void reset(ArrayList<scheduleData> mList){
        this.mData = mList;
        sortItems();
        notifyDataSetChanged();
    }
    public void setFragmentManager(FragmentManager fm){
        this.fm = fm;
    }
    public void setOnclickListener(View.OnClickListener listener){
        onClickListener = listener;
    }
    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴한다.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.schedule_item, parent, false);
        ScheduleRecyclerAdapter.ViewHolder vh = new ScheduleRecyclerAdapter.ViewHolder(view);

        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시한다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        scheduleData item = mData.get(position);
        holder.scheduleTitle.setText(item.getTitle());
        holder.scheduleTime.setText(item.getStartHour()+"시 "+item.getStartMinute()+"분" + " - "+item.getEndHour()+"시 "+item.getEndMinute()+"분");
        holder.scheduleIcon.setImageResource(iconResArray[holder.getAdapterPosition() % iconResArray.length]);
        holder.data = mData.get(position);
        holder.index = holder.getAdapterPosition();
        holder.selectedUser = selectedUser;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout scheduleIconAndTime;
        ImageView scheduleIcon, scheduleDelBtn;
        TextView scheduleTime, scheduleTitle;
        Context context;
        scheduleData data;
        userData selectedUser;
        int index;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            scheduleIconAndTime = itemView.findViewById(R.id.scheduleIconAndTime);
            scheduleIcon = itemView.findViewById(R.id.scheduleIcon);
            scheduleTime = itemView.findViewById(R.id.scheduleTime);
            scheduleTitle = itemView.findViewById(R.id.scheduleTitle);
            scheduleDelBtn = itemView.findViewById(R.id.scheduleDelBtn);
            scheduleDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.create();
                    alertDialog
                            .setTitle("정말 삭제하시겠습니까?")
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("ScheduleDeleteDialog", "삭제");
                                    removeAt(index);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("ScheduleDeleteDialog", "취소");
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
            // 일정 클릭시 수정 화면으로 이동
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("뷰홀더","선택된 제목: "+scheduleTitle.getText().toString()+", 시간: "+scheduleTime.getText().toString());
                    ScheduleTypeDialog dialog = ScheduleTypeDialog.newInstance(data, index,myApp, "");
                    dialog.show(fm, "일정수정");
//                    ScheduleTypeDialog dialog = new ScheduleTypeDialog(context, data);
//                    dialog.show("fragment_edit_name");
                }
            });
        }
    }
}
