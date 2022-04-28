package com.cookandroid.smartmirror.navigationFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.activities.MainScreenActivity;

import java.util.ArrayList;

public class MessageSendFragment extends Fragment {
    MainScreenActivity activity;
    // 프로필 정보 객체를 담아둔 ArrayList
    ArrayList<Profile> profileList = new ArrayList<>();
    Button testBtnRegister;
    // 현재 선택되어있는 프로필의 개수
    int selectedCount = 0;
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
    boolean selected = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_message_send, container, false);

        // 변수들
        final int imgViewWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        final int imgViewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        final GridLayout profileGridLayout = (GridLayout) rootView.findViewById(R.id.messageGrid);
        int selected_profile = -1;
        String profileName = "프로필1";
        boolean test1 = false;
        Profile test = new Profile("테스트", "프로필 "+1, false);
        testBtnRegister = rootView.findViewById(R.id.testBtnRegister);
        for(int i=0; i<2; i++) {

            Profile pData = new Profile("테스트", "프로필 "+i, false);
            profileList.add(pData);
            // 프로필 사진+프로필명 생성을 위한 LinLayout
            LinearLayout profile1 = new LinearLayout(rootView.getContext());
            LinearLayout.LayoutParams profileLinParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            profile1.setLayoutParams(profileLinParam);
            profile1.setGravity(Gravity.CENTER);
            profile1.setOrientation(LinearLayout.VERTICAL);
            // 프로필 사진(ImageView)
            int nowNum = i;
            ImageView profileImg = new ImageView(rootView.getContext());
            profileImg.setImageResource(R.drawable.ic_baseline_image_24);
            LinearLayout.LayoutParams imgViewParams = new LinearLayout.LayoutParams(imgViewWidth, imgViewHeight);
            profileImg.setLayoutParams(imgViewParams);
            profileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 해당 프로필이 선택되었음을 알려줘야 한다.
                    // 선택되어 있을 때
                    Profile nowProfile = profileList.get(nowNum);
                    if(nowProfile.isSelected){
                        // 선택되어있는 프로필을 클릭하면 선택이 풀린다.
//                        Log.i("test", "nowNum: "+nowNum);
                        profile1.setBackgroundColor(getResources().getColor(R.color.white));
                        nowProfile.isSelected = false;
                        selectedCount--;
                    }else{
                        // 선택이 안 되어있는 프로필을 클릭하면 클릭한 프로필이 선택된다.
  //                      Log.i("test", "selected: "+selected);
                        profile1.setBackgroundColor(getResources().getColor(R.color.bgColor));
                        nowProfile.isSelected = true;
                        selectedCount++;
                    }
                    Log.i("test", "selectedCount: "+selectedCount);


                }
            });

            // 프로필 명(TextView)
            TextView profileTextView = new TextView(rootView.getContext());
            LinearLayout.LayoutParams tViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            profileTextView.setLayoutParams(tViewParams);
            profileTextView.setText("프로필 "+(i+1));
            profileTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            // Layout에 추가
            profile1.addView(profileImg);
            profile1.addView(profileTextView);

            // GridLayout에 추가
            profileGridLayout.addView(profile1);
        }
        return rootView;
    }
    public class Profile{
        String imgSrc;
        String name;
        boolean isSelected;
        Profile(String imgSrc, String name, boolean isSelected){
            this.imgSrc = imgSrc;
            this.name = name;
            this.isSelected = isSelected;
        }
        void setSelected(boolean bool){
            this.isSelected = bool;
        }
    }
}