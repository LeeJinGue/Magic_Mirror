package com.cookandroid.smartmirror.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.MirrorDBHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.adapter.ProfileRecyclerAdapter;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.userData;

import java.sql.Array;
import java.util.ArrayList;

public class ProfileSelectActivity extends AppCompatActivity {
    ArrayList<LinearLayout> plsList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<userData> userDataList = new ArrayList<>();
    static int set_mode_check = 0;  //편집 or 이동모드 설정
    static int selectProfileNum = 0;    //선택한 profile 번호
    GridLayout profileGridLayout;
    ImageView setBtn, addBtn, profileSetting;
    MirrorDBHelper sqlDB;
    int imgViewWidth;
    int imgViewHeight;
    userData newUser;

    RecyclerView profileRecyclerView;
    ProfileRecyclerAdapter mAdapter;
    boolean isSettingMode;

    ActivityResultLauncher<Intent> StartForResultAddProfile = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        userData newUser = intent.getParcelableExtra("newUser");
                        Log.i("ProfileSelectActivity", "추가된 프로필: "+newUser.toString());

                        mAdapter.addItem(newUser);
                        mAdapter.changeImgViewList(true);
                        isSettingMode = false;

                    }else{

                    }
                }
            });
    ActivityResultLauncher<Intent> StartForResultEditProfile = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        userData editedUser = intent.getParcelableExtra("editUser");
                        int index = intent.getIntExtra("index", -1);
                        Log.i("ProfileSelectActivity", "수정된 프로필: "+editedUser.toString());
//                        userDataList.set(index, editedUser);
                        mAdapter.editItemNameAt(editedUser, index);
                        mAdapter.changeImgViewList(true);
                        isSettingMode = false;
                    }else if(result.getResultCode() == 100){
                        // 결과코드 100 == 해당 프로필을 삭제하려고 할 때
                        Intent intent = result.getData();
                        int index = intent.getIntExtra("index", -1);
                        userData delUser = intent.getParcelableExtra("delUser");
                        Log.i("ProfileSelectActivity", "삭제할 프로필"+delUser.toString());
                        mAdapter.delItemAt(delUser, index);
                        mAdapter.changeImgViewList(true);
                        isSettingMode = false;
                    }else{
//                        addedStockName = "없음";
                    }
                }
            });

    public ArrayList<userData> getUserData(){
        sqlDB = new MirrorDBHelper(getApplicationContext(), 1);
        ArrayList<userData> userDataList = sqlDB.getAllUserList();
        // 초기값
        userData last = new userData(2, "", "추가하기", "없음");
        userDataList.add(last);
        return userDataList;
    }

    // 이전버튼추가
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_select);
        MyApplication app = (MyApplication) getApplicationContext();
        nameList = app.getProfileNameList();
        profileRecyclerView = findViewById(R.id.profileRecyclerView);
        profileSetting = findViewById(R.id.profileSetting); // 수정 <-> 선택 모드변경 버튼

        // Add Coustom AppBar & Set Title Color Gradient
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbarTv);
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);

        // 프로필 RecyclerView 추가, 어댑터 추가
        userDataList = getUserData();
        mAdapter = new ProfileRecyclerAdapter(userDataList, getApplicationContext(), StartForResultEditProfile, StartForResultAddProfile);
        profileRecyclerView.setAdapter(mAdapter);
        profileRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL,false));
        isSettingMode = false; // 초기값이 false
        profileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfile();
            }
        });
    }

    void changeProfile() {
            // 세팅모드라면 프로필선택 모드로 바꾼다.
            // 프로필 선택 모드라면 세팅모드라 바꾼다.
        mAdapter.changeImgViewList(isSettingMode);
        if(isSettingMode){
            Log.i("ProfileSelectActivity", "모드를 변경합니다." + "선택모드 -> 수정모드");
            isSettingMode = false;
        }else{
            Log.i("ProfileSelectActivity", "모드를 변경합니다." +"수정모드 -> 선택모드");
            isSettingMode = true;
        }

    }
}