package com.cookandroid.smartmirror.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    ImageView setBtn, addBtn;
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
                        Log.i("프로필 추가 완료", "추가된 프로필: "+newUser.toString());
                        Log.i("프로필 추가 완료", "namesList: "+nameList.toString());
                        userDataList.add(newUser);
//                        mAdapter.addItem(newUser);
                        nameList.add(newUser.getName());
                        drawProfileList(nameList);
                    }else{
//                        addedStockName = "없음";
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
                        Log.i("프로필 수정 완료", "수정된 프로필: "+editedUser.toString());
                        userDataList.set(index, editedUser);
//                        mAdapter.editItemNameAt(editedUser.getName(), index);
                        nameList.set(index, userDataList.get(index).getName());
                        drawProfileList(nameList);

                    }else{
//                        addedStockName = "없음";
                    }
                }
            });
    @Override
    protected void onResume() {
        super.onResume();
//        MyApplication app = (MyApplication) getApplicationContext();
//        nameList = app.getProfileNameList();
    }
    public void getUserData(){
        sqlDB = new MirrorDBHelper(getApplicationContext(), 1);
//        userDataList = sqlDB.getAllUserList();

    }
    public void drawProfileList(ArrayList<String> nameList2){
        // 변수들
        imgViewWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        imgViewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        setBtn = findViewById(R.id.profileSetting);
        addBtn = findViewById(R.id.profileAdd);
        profileGridLayout = findViewById(R.id.profileGrid);
        if(nameList2.size()>1){
            for(int i=0; i<nameList2.size()-1;i++){
                profileGridLayout.removeViewAt(0);
            }
        }
        //DB에서 저장된 프로필 개수로 설정해야함
        for(int i=0; i< nameList2.size(); i++) {
            // 프로필 사진+프로필명 생성을 위한 LinLayout
            LinearLayout profile1 = new LinearLayout(this);
            LinearLayout.LayoutParams profileLinParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            profile1.setLayoutParams(profileLinParam);
            profile1.setGravity(Gravity.CENTER);
            profile1.setOrientation(LinearLayout.VERTICAL);

            // 프로필 사진(ImageView)
            ImageView profileImg = new ImageView(this);
            profileImg.setId(i+1);
            profileImg.setImageResource(R.drawable.ic_baseline_image_24);
            LinearLayout.LayoutParams imgViewParams = new LinearLayout.LayoutParams(imgViewWidth, imgViewHeight);
            profileImg.setLayoutParams(imgViewParams);
            profileImg.setOnClickListener(new OnClickListenerPutIndex(i) {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                    MyApplication app = (MyApplication) getApplicationContext();
                    app.setSelectedProfileName(nameList2.get(index));
                    System.out.println(nameList2.get(index)+"의 프로필이 선택되었습니다.");
                    startActivity(intent);

                }
            });

            // 프로필 명(TextView)
            TextView profileTextView = new TextView(this);
            LinearLayout.LayoutParams tViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            profileTextView.setLayoutParams(tViewParams);
            profileTextView.setText(nameList2.get(i));
            profileTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            // Layout에 추가
            profile1.addView(profileImg);
            profile1.addView(profileTextView);

            // GridLayout에 추가.
            profileGridLayout.addView(profile1, i);

            plsList.add(profile1);
        }
        //프로필추가 클릭 시
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 프로필 추가 화면으로 넘어갔다가 다시 돌아오므로 finish 불필요.
                selectProfileNum = 0;
                Intent intent = new Intent(getApplicationContext(), ProfileSettingActivity.class);
                intent.putExtra("mode", "add");
//                startActivity(intent);
                StartForResultAddProfile.launch(intent);
            }
        });

        //프로필 선택화면에서 click event로 기능 선택
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfile();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_select);
        MyApplication app = (MyApplication) getApplicationContext();
        nameList = app.getProfileNameList();
        profileGridLayout = findViewById(R.id.profileGrid);
//        profileRecyclerView = findViewById(R.id.profileRecyclerView);

        // Add Coustom AppBar & Set Title Color Gradient
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbarTv);
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);

        drawProfileList(nameList);

//        mAdapter = new ProfileRecyclerAdapter(userDataList, getApplicationContext());
//        profileRecyclerView.setAdapter(mAdapter);
//        profileRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL,false));
//        isSettingMode = false;

    }

    void changeProfile() {
            // 세팅모드라면 프로필선택 모드로 바꾼다.
            // 프로필 선택 모드라면 세팅모드라 바꾼다.

//        mAdapter.changeImgViewList(isSettingMode, StartForResultEditProfile);
//        isSettingMode = !isSettingMode;

        if(set_mode_check == 0) {
            // 0이라면 체크이미지로 되어있으므로 다시 그림으로 바꿔주고 클릭시 메인화면으로 넘어가게 한다.
            for(int i=0;i< plsList.size();i++) {
                ImageView imageView = plsList.get(i).findViewById(i+1);
//                System.out.println("profileImg 아이디 in 함수:"+imageView.getId());
                imageView.setImageResource(R.drawable.ic_baseline_image_24);
                imageView.setOnClickListener(new OnClickListenerPutIndex(i) {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                        MyApplication app = (MyApplication) getApplicationContext();
                        app.setSelectedProfileName(nameList.get(index));
                        System.out.println(nameList.get(index)+"의 프로필이 선택되었습니다.");
                        startActivity(intent);

                    }
                });
            }
            set_mode_check = 1;
        } else {
            // 0이라면 그림으로 되어있으므로 체크 이미지로 바꿔주고 클릭시 프로필 수정 화면으로 넘어간다.
            for(int i=0;i<plsList.size();i++) {
                ImageView imageView = plsList.get(i).findViewById(i+1);
                imageView.setImageResource(R.drawable.ic_baseline_offline_pin_24);
                imageView.setOnClickListener(new OnClickListenerPutIndex(i) {
                    @Override
                    public void onClick(View v) {
                        selectProfileNum = v.getId();
                        Intent intent = new Intent(getApplicationContext(), ProfileSettingActivity.class);
                        intent.putExtra("mode", "edit");
                        intent.putExtra("index", index);
                        intent.putExtra("editProfile", userDataList.get(index));
//                        intent.putExtra("userData", userDataList.get(index));
                        MyApplication app = (MyApplication) getApplicationContext();
                        app.setSelectedProfileName(nameList.get(index));
//                        startActivity(intent);
                        StartForResultEditProfile.launch(intent);

                    }
                });
            }
            set_mode_check = 0;
        }

    }
    public abstract static class OnClickListenerPutIndex implements View.OnClickListener{
        protected int index;
        public OnClickListenerPutIndex(int index){
            this.index = index;
        }
    }
}