package com.cookandroid.smartmirror.navigationFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.MirrorDBHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.adapter.ScheduleRecyclerAdapter;
import com.cookandroid.smartmirror.custom.ScheduleTypeDialog;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.scheduleData;
import com.cookandroid.smartmirror.dataClass.userData;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment implements ScheduleTypeDialog.EditScheduleDialogListener {
//    private ScheduleAdapter mAdapter;
    Context context;
    CalendarView calendarView;

    RecyclerView mRecyclerView = null;
    ScheduleRecyclerAdapter mAdapter = null;
    ArrayList<scheduleData> mList;
    Button scheduleAddBtn;
    MirrorDBHelper sqlDB;
    userData selectedUser;
    MyApplication myApp;
    String selectedDate;
    String nowDate; // 오늘날짜데이터
    public ArrayList<scheduleData> getScheduleDataList(String date){
        return sqlDB.getScheduleByDate(date, selectedUser);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        context = rootView.getContext();
        myApp = (MyApplication) getActivity().getApplicationContext();
        selectedUser = myApp.getSelectedUser();
        sqlDB = new MirrorDBHelper(context, 2);
        mList = new ArrayList<>();
        nowDate = Methods.getNowDate();
        mList = getScheduleDataList(nowDate);
        selectedDate = Methods.getNowDate(); // 처음엔 현재시간을 갖고온다.
        scheduleAddBtn = rootView.findViewById(R.id.scheduleAddBtn);
        calendarView = rootView.findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                Log.i("일정관리", year+"년 "+(month+1)+"월 "+dayOfMonth+"일이 선택되었습니다.");
                selectedDate =Methods.getDateStringFromInteger(year, month+1, dayOfMonth);
                Log.i("ScheduleFragment", "getDateStringFromInteger: "+selectedDate);

                mList = getScheduleDataList(selectedDate);
                mAdapter.reset(mList);
                // RecyclerView
//                mAdapter.addItem("테스트", 20, 00, 22,00, R.drawable.ic_schedule_orange);
//                mAdapter.notifyDataSetChanged();
                // ListView
//                adapter.loadDataWithDay();
//                adapter = new ScheduleAdapter();
//                scheduleListView.setAdapter(adapter);
            }
        });

        // RecyclerView
        mRecyclerView = rootView.findViewById(R.id.scheduleRecyclerView);
        mAdapter = new ScheduleRecyclerAdapter(mList, sqlDB, myApp);
        mAdapter.setFragmentManager(getChildFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));


        // 스케줄추가버튼
        scheduleAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getChildFragmentManager();
                ScheduleTypeDialog dialog = ScheduleTypeDialog.newInstance(null, -1,myApp, selectedDate);
                dialog.show(fm, "일정추가");
            }
        });
        return rootView;
    }


    @Override
    public void onFinishedEditDialog(int index, scheduleData data) {
        mAdapter.editItem(index, data);
    }

    @Override
    public void onFinishedAddDialog(scheduleData data) {
        Toast.makeText(getContext(), "일정이 추가되었습니다.", Toast.LENGTH_SHORT).show();
        data.setSchedule_id(++myApp.scheduleId);
        mAdapter.addItem(data);
    }

}