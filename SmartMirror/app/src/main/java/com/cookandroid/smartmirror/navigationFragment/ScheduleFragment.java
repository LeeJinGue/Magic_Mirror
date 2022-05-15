package com.cookandroid.smartmirror.navigationFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.adapter.ScheduleAdapter;
import com.cookandroid.smartmirror.adapter.ScheduleRecyclerAdapter;
import com.cookandroid.smartmirror.custom.ScheduleTypeDialog;
import com.cookandroid.smartmirror.dataClass.scheduleData;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment implements ScheduleTypeDialog.EditScheduleDialogListener {
//    private ScheduleAdapter mAdapter;
    Context context;
    CalendarView calendarView;
    ScheduleAdapter adapter;
    ListView scheduleListView;

    RecyclerView mRecyclerView = null;
    ScheduleRecyclerAdapter mAdapter = null;
    ArrayList<scheduleData> mList;
    Button scheduleAddBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        context = rootView.getContext();
        scheduleAddBtn = rootView.findViewById(R.id.scheduleAddBtn);
        calendarView = rootView.findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.i("일정관리", year+"년 "+month+"월 "+dayOfMonth+"일이 선택되었습니다.");

                // RecyclerView
                mAdapter.addItem("테스트", 20, 00, 22,00, R.drawable.ic_schedule_orange);
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
        mAdapter.setFragmentManager(getChildFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));
        mAdapter.addItem("제목1", 11, 00, 12, 00, R.drawable.ic_schedule_black);
        mAdapter.addItem("제목2", 13, 00, 19, 00, R.drawable.ic_schedule_purple);
        mAdapter.notifyDataSetChanged();

        // 스케줄추가버튼
        scheduleAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getChildFragmentManager();
                ScheduleTypeDialog dialog = ScheduleTypeDialog.newInstance(null, -1);
                dialog.show(fm, "일정추가");
            }
        });


        return rootView;
    }


    @Override
    public void onFinishedEditDialog(int index, scheduleData data) {
        mAdapter.editItem(index, data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishedAddDialog(scheduleData data) {
        Toast.makeText(getContext(), "일정이 추가되었습니다.", Toast.LENGTH_SHORT).show();
        mAdapter.addItem(data.getTitle(), data.getStartHour(), data.getStartMinute(), data.getEndHour(), data.getEndMinute(), R.drawable.ic_schedule_green);
        mAdapter.notifyDataSetChanged();
    }

}