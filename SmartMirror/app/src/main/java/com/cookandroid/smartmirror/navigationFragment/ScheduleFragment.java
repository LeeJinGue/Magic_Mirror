package com.cookandroid.smartmirror.navigationFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.adapter.ScheduleAdapter;
import com.cookandroid.smartmirror.adapter.ScheduleRecyclerAdapter;
import com.cookandroid.smartmirror.custom.ScheduleTypeDialog;
import com.cookandroid.smartmirror.dataClass.scheduleData;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {
//    private ScheduleAdapter mAdapter;
    CalendarView calendarView;
    ScheduleAdapter adapter;
    ListView scheduleListView;

    RecyclerView mRecyclerView = null;
    ScheduleRecyclerAdapter mAdapter = null;
    ArrayList<scheduleData> mList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        calendarView = rootView.findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.i("일정관리", year+"년 "+month+"월 "+dayOfMonth+"일이 선택되었습니다.");

                // RecyclerView
                mAdapter.addItem("테스트", "20시", "22시", R.drawable.ic_schedule_orange);
                mAdapter.notifyDataSetChanged();
                // ListView
//                adapter.loadDataWithDay();
//                adapter = new ScheduleAdapter();
//                scheduleListView.setAdapter(adapter);
            }
        });

        // RecyclerView
        mRecyclerView = rootView.findViewById(R.id.scheduleRecyclerView);
        mList = new ArrayList<>();
        mAdapter = new ScheduleRecyclerAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));
        mAdapter.addItem("제목1", "11시", "12시", R.drawable.ic_schedule_black);
        mAdapter.addItem("제목2", "13시", "19시", R.drawable.ic_schedule_purple);
        mAdapter.notifyDataSetChanged();

        // ListView
//        adapter = new ScheduleAdapter();
//
//        scheduleListView = (ListView)rootView.findViewById(R.id.scheduleListView);
//        scheduleListView.setAdapter(adapter);
//        adapter.addItem("제목1","11시", "12시", R.drawable.ic_schedule_black);
//        adapter.addItem("2제목","21시", "22시", R.drawable.ic_schedule_black);


        return rootView;
    }


}