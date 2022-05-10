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

import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.custom.ScheduleTypeDialog;
import com.cookandroid.smartmirror.dataClass.scheduleData;

import java.util.ArrayList;

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder> {
    ArrayList<scheduleData> mData = new ArrayList<>();
    FragmentManager fm;
    View.OnClickListener onClickListener;

    public ScheduleRecyclerAdapter(ArrayList<scheduleData> mList){
        mData = mList;
    }
    public void addItem(String title, int startHour, int startMinute, int endHour, int endMinute,int iconRes){
        scheduleData sData = new scheduleData(title, iconRes, startHour, startMinute, endHour, endMinute);
        mData.add(sData);
    }
    public void editItem(int index, scheduleData data){
        mData.set(index, data);
    }
    public void removeAt(int index){
        mData.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, mData.size());
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
        holder.scheduleIcon.setImageResource(item.getIconRes());
        holder.data = mData.get(position);
        holder.index = holder.getAdapterPosition();
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
                    ScheduleTypeDialog dialog = ScheduleTypeDialog.newInstance(data, index);
                    dialog.show(fm, "일정수정");
//                    ScheduleTypeDialog dialog = new ScheduleTypeDialog(context, data);
//                    dialog.show("fragment_edit_name");
                }
            });
        }
    }
}
