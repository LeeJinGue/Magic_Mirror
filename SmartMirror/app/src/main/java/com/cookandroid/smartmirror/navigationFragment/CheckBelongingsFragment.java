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
import android.widget.ImageView;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.MirrorDBHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.activities.BelongingSetAddActivity;
import com.cookandroid.smartmirror.adapter.BelongingSetRecyclerAdapter;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.belongingSetData;
import com.cookandroid.smartmirror.dataClass.userData;

import java.util.ArrayList;


public class CheckBelongingsFragment extends Fragment {
    RecyclerView belongingSetRecyclerView;
    Context context;
    ImageView belongingSetAddBtn;
    ArrayList<belongingSetData> mList;
    BelongingSetRecyclerAdapter mAdapter;
    MyApplication myApp;
    userData selectedUser;
    MirrorDBHelper sqlDB;
    belongingSetData addedBelongingSet, editedBelongingSet;
    ActivityResultLauncher<Intent> mStartForResultAddSet = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i("result체크", "result: "+result.toString()+", resultCode: "+result.getResultCode());

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        addedBelongingSet = intent.getParcelableExtra("belongingSet");
                        mAdapter.addItem(addedBelongingSet);
//                        Log.i("CheckBelongingsFragment", "소지품세트 추가, 소지품정보:\n"+addedBelongingSet.toString());

                    }else{
                        addedBelongingSet = null;
                    }
                }
            });
    ActivityResultLauncher<Intent> mStartForResultEditSet = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
//                    Log.i("CheckBelongingsFragment", "onActivityResult 호출 - 수정");

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        editedBelongingSet = intent.getParcelableExtra("belongingSet");
                        int index = intent.getIntExtra("index", -1);
                        if(index != -1){
//                            Log.i("CheckBelongingsFragment", index+"번째 소지품세트 수정, 소지품정보:\n"+editedBelongingSet.toString());
                            mAdapter.editItem(index, editedBelongingSet);
                        }else{
                            Log.i("CheckBelongingsFragment", index+"번째, 수정할 인덱스가 -1?"+editedBelongingSet.toString());

                        }
                    }else{
                        editedBelongingSet = null;
                    }
                }
            });
    public ArrayList<belongingSetData> getBelongingSetListWithUser(userData selectedUser){
        ArrayList<belongingSetData> belongingSetData = sqlDB.getBelongingSetDataByUser(selectedUser);
        return belongingSetData;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_check_belongings, container, false);
        context = rootView.getContext();
        myApp = (MyApplication) getActivity().getApplicationContext();
        selectedUser = myApp.getSelectedUser();
        sqlDB = new MirrorDBHelper(context, 1);
        mList = getBelongingSetListWithUser(selectedUser);
        belongingSetRecyclerView = rootView.findViewById(R.id.belongingSetRecyclerView);
        belongingSetAddBtn = rootView.findViewById(R.id.belongingSetAddBtn);
        belongingSetAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 소지품 프리셋 추가 액티비티로 전환
                // 전달되는 데이터 X
                Intent intent = new Intent(context, BelongingSetAddActivity.class);
                intent.putExtra("isAdd", true);
                mStartForResultAddSet.launch(intent);
            }
        });

        mAdapter = new BelongingSetRecyclerAdapter(mList, sqlDB);
        mAdapter.setFragmentContext(rootView.getContext());
        mAdapter.setmStartForResult(mStartForResultEditSet);
        belongingSetRecyclerView.setAdapter(mAdapter);
        belongingSetRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false));
        belongingSetRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));
        return rootView;
    }
}