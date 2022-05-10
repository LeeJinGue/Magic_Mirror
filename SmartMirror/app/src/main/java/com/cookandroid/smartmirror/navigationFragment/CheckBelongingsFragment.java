package com.cookandroid.smartmirror.navigationFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.activities.BelongingSetAddActivity;
import com.cookandroid.smartmirror.adapter.BelongingSetRecyclerAdapter;
import com.cookandroid.smartmirror.dataClass.belongingSetData;

import java.util.ArrayList;


public class CheckBelongingsFragment extends Fragment {
    RecyclerView belongingSetRecyclerView;
    Context context;
    Button belongingAddBtn;
    ArrayList<belongingSetData> mList;
    BelongingSetRecyclerAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_belongings, container, false);
        context = rootView.getContext();
        belongingSetRecyclerView = rootView.findViewById(R.id.belongingSetRecyclerView);
        belongingAddBtn = rootView.findViewById(R.id.belongingAddBtn);
        belongingAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 소지품 프리셋 추가 액티비티로 전환
                // 전달되는 데이터 X
                Intent intent = new Intent(context, BelongingSetAddActivity.class);
                startActivity(intent);
            }
        });
        mList = new ArrayList<>();
        mAdapter = new BelongingSetRecyclerAdapter(mList);
        belongingSetRecyclerView.setAdapter(mAdapter);
        belongingSetRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false));
        belongingSetRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));
        ArrayList<String> belongItemList = new ArrayList<>();
        belongItemList.add("소지품1");
        belongItemList.add("소지품2");
        belongItemList.add("소지품3");
        belongItemList.add("소지품4");
        belongItemList.add("소지품5");
        mAdapter.addItem(new belongingSetData("소지품세트1", "테스트용", (ArrayList<String>) belongItemList.clone()));
        belongItemList.remove(4);
        belongItemList.add("테스트2");
        mAdapter.addItem(new belongingSetData("소지품세트222", "테스트용22", (ArrayList<String>) belongItemList.clone()));
        mAdapter.notifyDataSetChanged();
        return rootView;
    }
}