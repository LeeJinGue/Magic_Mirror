package com.cookandroid.smartmirror.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookandroid.smartmirror.helper.MethodsHelper;
import com.cookandroid.smartmirror.helper.MirrorDBHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.adapter.MessageRecyclerAdapter;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.messageData;
import com.cookandroid.smartmirror.dataClass.userData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class MessageCheckActivity extends AppCompatActivity {
    ImageView sendMessageBtn;
//    String selectedProfile;
    Context context;
    ArrayList<messageData> messageDataList = new ArrayList<>();
    LinearLayout messageListLayout;
    LinearLayout.LayoutParams messageLayoutParams, receivedMessageParams, sendedMessageParams, dateParams;
    int messagePadding, messageWidth, dp;
    EditText messageEditText;
    userData messageReceiverUser, selectedUser;
    MirrorDBHelper sqlDB;
    ArrayList<messageData> receivedMessageList, sendedMessageList, allMessageList;
    MessageRecyclerAdapter mAdapter;
    RecyclerView msgRecyclerView;
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
                messageEditText.clearFocus();
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
        setContentView(R.layout.activity_message_check);
        context = getApplicationContext();
        messageReceiverUser = getIntent().getParcelableExtra("messageReceiverUser");
        sqlDB = new MirrorDBHelper(context, 2);
        MyApplication myapp = (MyApplication)getApplicationContext();
        // 현재 로그인되어있는 유저
        selectedUser = myapp.getSelectedUser();

        allMessageList = getMessageDataList();
        messageEditText = findViewById(R.id.messageEditText);
        msgRecyclerView = findViewById(R.id.messageRecyclerView);
        mAdapter = new MessageRecyclerAdapter(context, allMessageList);
        msgRecyclerView.setAdapter(mAdapter);
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));


        // Add Coustom AppBar & Set Title Color Gradient
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbarTv);
        tvTitle.setText(messageReceiverUser.getName()+"님의 메시지");
        MethodsHelper methodsHelper = new MethodsHelper();
        methodsHelper.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼
        ab.setDisplayHomeAsUpEnabled(true);

        sendMessageBtn = findViewById(R.id.messageChecksendMessage);
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();

                LocalDateTime nowTime = LocalDateTime.now();
                String dateTimeString = nowTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                messageData newMsg = new messageData(1, messageReceiverUser.getUser_num(), selectedUser.getUser_num(), messageText, dateTimeString, false);
                mAdapter.addMessage(newMsg);
                messageEditText.setText("");
                Log.i("MessageCheckActivity", "메시지 전송");
            }
        });
        Log.i("MessageCheckActivity", "선택된 프로필 명: "+messageReceiverUser.getName());

    }

    // 보낸 사람, 받는 사람에 따른 메시지 데이터를 받아온다.
    public ArrayList<messageData> getMessageDataList(){
        // 1. 보낸사람, 받는사람, 날짜에 따른 메시지 데이터를 받아온다.
        // 2. 메시지를 시간 순으로 정렬한다.

        // 내가 보낸 리스트, 상대한테 받은 리스트 다 받아옴
        ArrayList<messageData> messageDataList = new ArrayList<messageData>();
        messageDataList = sqlDB.getMeesageList(selectedUser, messageReceiverUser, false);
        messageDataList.addAll(sqlDB.getMeesageList(selectedUser, messageReceiverUser, true));
        // 받아온 메시지 데이터를 시간순으로 정렬합니다.
        messageDataList.sort(new MessageDateTimeComparator());
        // 메시지가 없으면 추가X
        if(messageDataList.isEmpty()) return messageDataList;

        // 날짜가 달라지면 날짜객체를 추가합니다.
        messageData firstDate = new messageData(messageDataList.get(0).getDate());
        messageDataList.add(0, firstDate);
        for(int i=1; i<messageDataList.size()-1; i++){
            messageData now = messageDataList.get(i);
            messageData next = messageDataList.get(i+1);
            // 년/월/일 중 하나라도 다르면 다른날짜임
            if(now.getYear() != next.getYear() && now.getMonth() != next.getMonth() && now.getDay() != next.getDay()){
                messageData dateData = new messageData(next.getDate());
                messageDataList.add(i+1, dateData);
                i++;
                Log.i("날짜데이터",next.getYear()+"년 "+ next.getMonth()+"월 "+ next.getDate()+"일 추가");
            }
        }
        return messageDataList;
    }
    public class MessageDateTimeComparator implements Comparator<messageData>{
        @Override
        public int compare(messageData o1, messageData o2) {
            // 연도비교
            if(o1.getYear() > o2.getYear()){
                return 1;
            }else if(o1.getYear() < o2.getYear()){
                return -1;
            }else{
                // 월비교
                if(o1.getMonth() > o2.getMonth()){
                    return 1;
                }else if(o1.getMonth() < o2.getMonth()){
                    return -1;
                }else{
                    // 일비교
                    if(o1.getDay() > o2.getDay()){
                        return 1;
                    }else if(o1.getDay() < o2.getDay()){
                        return -1;
                    }else{
                        // 연월일 다 같은데 뒤에꺼가 날짜 텍스트뷰다 -> 뒤에꺼를 앞으로
                        if(o2.getViewType() == R.integer.TYPE_DATE) return 1;
                        // 시간비교
                        if(o1.getHour() > o2.getHour()){
                            return 1;
                        }else if(o1.getHour() < o2.getHour()){
                            return -1;
                        }else{
                            // 분비교
                            if(o1.getMinute() > o2.getMinute()){
                                return 1;
                            }else if(o1.getMinute() < o2.getMinute()){
                                return -1;
                            }else{
                                // 아예 같다면 1
                                return 1;
                            }
                        }
                    }
                }
            }
        }
    }
    public class MessageDateComparator implements Comparator<messageData>{
        @Override
        public int compare(messageData o1, messageData o2) {
            if(o1.getYear() > o2.getYear()){
                return 1;
            }else if(o1.getYear() < o2.getYear()){
                return -1;
            }else{
                if(o1.getMonth() > o2.getMonth()){
                    return 1;
                }else if(o1.getMonth() < o2.getMonth()){
                    return -1;
                }else{
                    if(o1.getDay() > o2.getDay()){
                        return 1;
                    }else if(o1.getDay() < o2.getDay()){
                        return -1;
                    }else{
                        // 아예 같다면 1
                        return 1;
                    }
                }
            }
        }
    }
    public class MessageTimeComparator implements Comparator<messageData>{
        @Override
        public int compare(messageData o1, messageData o2) {
            if(o1.getHour() > o2.getHour()){
                return 1;
            }else if(o1.getHour() < o2.getHour()){
                return -1;
            }else{
                if(o1.getMinute() > o2.getMinute()){
                    return 1;
                }else if(o1.getMinute() < o2.getMinute()){
                    return -1;
                }else{
                    // 아예 같다면 1
                    return 1;
                }
            }
        }
    }
}