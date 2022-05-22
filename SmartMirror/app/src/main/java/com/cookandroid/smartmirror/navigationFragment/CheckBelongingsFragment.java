package com.cookandroid.smartmirror.navigationFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
    belongingSetData addedBelongingSet, editedBelongingSet;
    ActivityResultLauncher<Intent> mStartForResultAddSet = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i("CheckBelongingsFragment", "onActivityResult 호출 - 추가");

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        addedBelongingSet = intent.getParcelableExtra("belongingSet");
                        mAdapter.addItem(addedBelongingSet);
                        Log.i("CheckBelongingsFragment", "소지품세트 추가, 소지품정보:\n"+addedBelongingSet.toString());

                    }else{
                        addedBelongingSet = null;
                    }
                }
            });
    ActivityResultLauncher<Intent> mStartForResultEditSet = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i("CheckBelongingsFragment", "onActivityResult 호출 - 수정");

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        editedBelongingSet = intent.getParcelableExtra("belongingSet");
                        int index = intent.getIntExtra("index", -1);
                        if(index != -1){
                            Log.i("CheckBelongingsFragment", index+"번째 소지품세트 수정, 소지품정보:\n"+editedBelongingSet.toString());
                            mAdapter.editItem(index, editedBelongingSet);
                        }else{
                            Log.i("CheckBelongingsFragment", index+"번째, 수정할 인덱스가 -1?"+editedBelongingSet.toString());

                        }
                    }else{
                        editedBelongingSet = null;
                    }
                }
            });
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("CheckBelongingsFragment", "뷰생성");
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
                intent.putExtra("isAdd", true);
                mStartForResultAddSet.launch(intent);
            }
        });

        mList = new ArrayList<>();
        mAdapter = new BelongingSetRecyclerAdapter(mList);
        mAdapter.setFragmentContext(rootView.getContext());
        mAdapter.setmStartForResult(mStartForResultEditSet);
        belongingSetRecyclerView.setAdapter(mAdapter);
        belongingSetRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false));
        belongingSetRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));
        ArrayList<String> belongItemList = new ArrayList<>();
        belongItemList.add("지갑");
        belongItemList.add("핸드폰");
        belongItemList.add("핸드크림");
        belongItemList.add("립밤");
        belongItemList.add("USB");
        mAdapter.addItem(new belongingSetData("월요일", "수업 때 챙길 물품", (ArrayList<String>) belongItemList.clone()));
        belongItemList.remove(4);
        belongItemList.add("노트북");
        mAdapter.addItem(new belongingSetData("화요일", "Java", (ArrayList<String>) belongItemList.clone()));
        mAdapter.addItem(new belongingSetData("수요일", "C++", (ArrayList<String>) belongItemList.clone()));
        mAdapter.addItem(new belongingSetData("목요일", "공강", (ArrayList<String>) belongItemList.clone()));
        mAdapter.addItem(new belongingSetData("금요일", "블록체인", (ArrayList<String>) belongItemList.clone()));
        mAdapter.addItem(new belongingSetData("토요일", "휴식", (ArrayList<String>) belongItemList.clone()));
        mAdapter.addItem(new belongingSetData("일요일", "휴식", (ArrayList<String>) belongItemList.clone()));
        mAdapter.notifyDataSetChanged();
        return rootView;
    }
}