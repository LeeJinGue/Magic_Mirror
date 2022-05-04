package com.cookandroid.smartmirror.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.scheduleData;

import java.util.ArrayList;

public class ScheduleAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<scheduleData> scheduleDataList = new ArrayList<scheduleData>();

    public ScheduleAdapter() {
//            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void clear(){
        scheduleDataList.clear();
    }
    public void loadDataWithDay(){
        scheduleDataList.clear();
        scheduleData sData = new scheduleData("테스트", R.drawable.ic_schedule_orange, "11시", "12시");
        scheduleDataList.add(sData);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return scheduleDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return scheduleDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void addItem(String title, String startTime, String endTime, int iconRes){
        scheduleData sData = new scheduleData(title, iconRes, startTime, endTime);
        scheduleDataList.add(sData);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            scheduleData scheduleData = scheduleDataList.get(position);

            convertView = inflater.inflate(R.layout.schedule_item, parent, false);
            TextView schduleTime = convertView.findViewById(R.id.scheduleTime);
//            TextView schedultDetail = convertView.findViewById(R.id.scheduleDetail);
            TextView schedultTitle = convertView.findViewById(R.id.scheduleTitle);
            ImageView scheduleIcon = convertView.findViewById(R.id.scheduleIcon);
            schduleTime.setText(scheduleData.getStartTime() + " - " + scheduleData.getEndTiem());
//            schedultDetail.setText(scheduleData.getDetail());
            schedultTitle.setText(scheduleData.getTitle());
            scheduleIcon.setImageResource(scheduleData.getIconRes());
        }
        return convertView;
//            TextView textView = null;
//            if (convertView == null) {
//                convertView = mInflater.inflate(R.layout.schedule_item, null);
//                textView = (TextView) convertView.findViewById(R.id.scheduleTitle);
//                convertView.setTag(textView);
//            } else {
//                textView = (TextView) convertView.getTag();
//            }
//            textView.setText(scheduleDataList.get(position));
//            return convertView;
    }
}