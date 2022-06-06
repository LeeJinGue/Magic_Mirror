package com.cookandroid.smartmirror.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cookandroid.smartmirror.helper.MethodsHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.userData;

public class ProfileSettingActivity extends AppCompatActivity {
    EditText name;
    Button regBtn, delBtn;
    String mode;
    int index;
    MyApplication app;
    EditText profileName;
    TextView tv;
    userData editProfile;
    // EditText ?
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if(view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")){
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if(x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()){
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
                profileName.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
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
        setContentView(R.layout.activity_profile_setting);
        profileName = findViewById(R.id.editSerial2);
        tv = findViewById(R.id.profileSettingTextView);
        // Add Coustom AppBar & Set Title Color Gradient
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbarTv);
        MethodsHelper methodsHelper = new MethodsHelper();
        methodsHelper.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);

        app = (MyApplication) getApplicationContext();
        Intent i = getIntent();
        mode = i.getStringExtra("mode");

        name = (EditText) findViewById(R.id.editSerial2);
        regBtn = (Button) findViewById(R.id.profileRegisterBtn);
        delBtn = (Button) findViewById(R.id.profileDelBtn);
        if(mode.equals("add")){
            // 프로필 추가 모드
            Log.i("ProfileSettingActivity", "프로필 추가 모드");
            regBtn.setText("등록");
            regBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 추가할 프로필 이름,사진을 입력하고 등록버튼을 누르면 다시 프로필 선택 화면으로 돌아간다.
                    app.addProfileName(name.getText().toString());
                    String userName = name.getText().toString();
                    userData newUser = new userData(2, "2", userName, "/bin");
                    Intent intent = new Intent();
                    intent.putExtra("newUser", newUser);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }else if(mode.equals("edit")){
            Log.i("ProfileSettingActivity", "프로필 편집 모드");

            // 프로필 편집 모드
            index = i.getIntExtra("index", -1);
            editProfile = i.getParcelableExtra("editProfile");
            profileName.setText(editProfile.getName());
            delBtn.setVisibility(View.VISIBLE);
            tv.setText("프로필을 편집합니다.");
            regBtn.setText("편집");
            regBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 편집할 프로필 이름,사진을 입력하고 등록버튼을 누르면 다시 프로필 선택 화면으로 돌아간다.
                    String userName = name.getText().toString();
                    userData editedUser = new userData(editProfile.getUser_num(), editProfile.getSerial_no(), userName, editProfile.getUser_image_pass());
                    Intent intent = new Intent();
                    intent.putExtra("index", index);
                    intent.putExtra("editUser", editedUser);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 프로필을 삭제합니다. 삭제할 프로필 객체와 인덱스를 함꼐 보냅니다.
                    // 결과 코드는 100
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    alertDialog.create();
                    alertDialog
                            .setTitle("정말 삭제 하시겠습니까?")
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("profileDelDialog", editProfile.getName()+"유저 삭제");
                                    dialog.dismiss();
                                    Intent intent = new Intent();
                                    intent.putExtra("delUser", editProfile);
                                    intent.putExtra("index", index);
                                    setResult(100, intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("messageDelDialog", "취소");
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    Log.i("test", "삭제버튼 클릭");



                }
            });
        }else{
            Log.i("ProfileSettingActivity", "모드 에러");
        }

        //

    }
}
