package com.cookandroid.smartmirror.navigationFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cookandroid.smartmirror.helper.MirrorDBHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.activities.MainScreenActivity;
import com.cookandroid.smartmirror.activities.MessageCheckActivity;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.userData;

import java.util.ArrayList;

public class MessageSendFragment extends Fragment {
    MainScreenActivity activity;
    ArrayList<LinearLayout> plsList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    GridLayout messageGridLayout;
    Button messageSendBtn;
    EditText editSendMessage;
    Context context;
    ViewGroup rootView;
    int imgViewWidth,imgViewHeight, selectedProfileIndex;
    boolean isProfileSelected;
    String sendProfileName;
    MirrorDBHelper sqlDB;
    userData selectedUser;
    ArrayList<userData> userListWithOutSelectedUser = new ArrayList<>();
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainScreenActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_message_send, container, false);
        context = container.getContext();

        MyApplication app = (MyApplication)context.getApplicationContext();
        sqlDB = new MirrorDBHelper(rootView.getContext(), 2);

        nameList = app.getProfileNameList();
        selectedUser = app.getSelectedUser();
        // 선택된 프로필을 제외한 다른 프로필 리스트
        // 먼저 모든 유저 리스트를 받아오고 본인일 경우 리스트에서 지워줍니다.
        userListWithOutSelectedUser = sqlDB.getAllUserList();
        userListWithOutSelectedUser.removeIf(user -> user.getUser_num() == selectedUser.getUser_num());
//        String userListTest = "userList without me: ";
//        for(userData user:userListWithOutSelectedUser){
//            userListTest+= user.toString() + "\n";
//        }
//        Log.i("userList Test", userListTest);

        // 선택된 프로필이 있나요?
        isProfileSelected = false;
        // 선택된 프로필의 인덱스는?(-1일 경우 선택안되어있다는 뜻)
        selectedProfileIndex = -1;
        sendProfileName = "";
        drawProfileList(userListWithOutSelectedUser);

        return rootView;
    }
    public void drawProfileList(ArrayList<userData> userListWithOutSelectedUser){
        // 변수들
        imgViewWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        imgViewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        messageGridLayout = rootView.findViewById(R.id.messageGrid);

        //DB에서 저장된 프로필 개수로 설정해야함
        for(int i=0; i< userListWithOutSelectedUser.size(); i++) {
            // 프로필 사진+프로필명 생성을 위한 LinLayout
            LinearLayout profile1 = new LinearLayout(context);
            LinearLayout.LayoutParams profileLinParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            profile1.setLayoutParams(profileLinParam);
            profile1.setGravity(Gravity.CENTER);
            profile1.setOrientation(LinearLayout.VERTICAL);

            // 프로필 사진(ImageView)
            ImageView profileImg = new ImageView(context);
            profileImg.setId(i+1);
            profileImg.setImageResource(R.drawable.ic_baseline_image_24);
            LinearLayout.LayoutParams imgViewParams = new LinearLayout.LayoutParams(imgViewWidth, imgViewHeight);
            profileImg.setLayoutParams(imgViewParams);
            profileImg.setOnClickListener(new OnClickListenerPutIndex(i) {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MessageCheckActivity.class);
//                    intent.putExtra("selectedProfile", nameList.get(index));
                    intent.putExtra("messageReceiverUser", userListWithOutSelectedUser.get(index));
                    startActivity(intent);

//                    // 중복 선택이 안된다고 가정한다.
//                    MyApplication app = (MyApplication) context.getApplicationContext();
//                    app.setSelectedProfileName(nameList.get(index));
//                    Log.i("메시지전송", "프로필이 선택되어있는가?:"+isProfileSelected+", 선택된 프로필 인덱스:"+selectedProfileIndex);
//                    if(isProfileSelected){
//                        // 어떤 프로필이 선택되어있는 경우,
//                        if(selectedProfileIndex == index){
//                            // 선택된거랑 지금고른거랑 같으면 선택을 해제한다.
//                            plsList.get(selectedProfileIndex).setBackgroundColor(getResources().getColor(R.color.white));
//                            isProfileSelected = false;
//                            selectedProfileIndex = -1;
//                            sendProfileName = "";
//                        }else {
//                            // 그 프로필의 선택을 해제하고 지금 고른거를 선택한다.
//                            // 1. 선택되어있는거 해제
//                            plsList.get(selectedProfileIndex).setBackgroundColor(getResources().getColor(R.color.white));
//                            // 2. 지금 누른거 선택
//                            plsList.get(index).setBackgroundColor(getResources().getColor(R.color.bgColor));
//                            isProfileSelected = true;
//                            selectedProfileIndex = index;
//                            sendProfileName = app.getProfileName(index);
//                        }
//                    }else{
//                        // 선택 안되어있으면 그냥 지금 누른거를 선택한다.
//                        plsList.get(index).setBackgroundColor(getResources().getColor(R.color.bgColor));
//                        isProfileSelected = true;
//                        selectedProfileIndex = index;
//                        sendProfileName = app.getProfileName(index);
//                    }
//                    System.out.println(nameList.get(index)+"의 프로필이 선택되었습니다.");
////                    plsList.get(index).setBackgroundColor(getResources().getColor(R.color.white));
////                    profile1.setBackgroundColor(getResources().getColor(R.color.white));
//                    Log.i("메시지전송", "바뀐내용:\n프로필이 선택되어있는가?:"+isProfileSelected+", 선택된 프로필 인덱스:"+selectedProfileIndex);

                }
            });

            // 프로필 명(TextView)
            TextView profileTextView = new TextView(context);
            LinearLayout.LayoutParams tViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            profileTextView.setLayoutParams(tViewParams);
            profileTextView.setText(userListWithOutSelectedUser.get(i).getName());
            profileTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            // Layout에 추가
            profile1.addView(profileImg);
            profile1.addView(profileTextView);

            // GridLayout에 추가.
            messageGridLayout.addView(profile1, i);
            plsList.add(profile1);
        }

    }
    public abstract class OnClickListenerPutIndex implements View.OnClickListener{
        protected int index;
        public OnClickListenerPutIndex(int index){
            this.index = index;
        }
    }

}
