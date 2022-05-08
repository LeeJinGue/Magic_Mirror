package com.cookandroid.smartmirror.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.messageData;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class MessageCheckActivity extends AppCompatActivity {
    ImageView sendMessageBtn;
    String selectedProfile;
    Context context;
    ArrayList<messageData> messageDataList = new ArrayList<>();
    LinearLayout messageListLayout;
    LinearLayout.LayoutParams messageLayoutParams, receivedMessageParams, sendedMessageParams, dateParams;
    int messagePadding, messageWidth, dp;


    public static int ConvertDPtoPX(Context context, int dp) { float density = context.getResources().getDisplayMetrics().density; return Math.round((float) dp * density); }

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
        selectedProfile = getIntent().getStringExtra("selectedProfile");

        // width/padding값 설정
        dp=ConvertDPtoPX(context, 1);
        messageWidth = ConvertDPtoPX(context, 180);
        messagePadding = ConvertDPtoPX(context,5);

        // Layout Params 설정
        messageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        receivedMessageParams = new LinearLayout.LayoutParams(messageWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        receivedMessageParams.gravity = Gravity.LEFT;
        receivedMessageParams.rightMargin = dp*10;
        sendedMessageParams = new LinearLayout.LayoutParams(messageWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        sendedMessageParams.gravity = Gravity.RIGHT;
        sendedMessageParams.leftMargin = dp*10;
        dateParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dateParams.gravity = Gravity.BOTTOM;

        // Add Coustom AppBar & Set Title Color Gradient
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbarTv);
        tvTitle.setText(selectedProfile+"님의 메시지");
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);

        messageListLayout = findViewById(R.id.messageListLayout);
        sendMessageBtn = findViewById(R.id.messageChecksendMessage);
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MessageCheckActivity", "메시지 전송");
            }
        });
        Log.i("MessageCheckActivity", "선택된 프로필 명: "+selectedProfile);
        addReceivedMessage("상대가보낸메시지 상대가보낸메시지 상대가보낸메시지 상대가보낸메시지 ", "오전 11:00");
        addSendedMessage("내가 보낸 메시지 내가 보낸 메시지 내가 보낸 메시지 내가 보낸 메시지 내가 보낸 메시지 ", "오전 12:00");
        getMessageDataList();
    }

    // 보낸 사람, 받는 사람에 따른 메시지 데이터를 받아온다.
    public void getMessageDataList(){
        // 1. 보낸사람, 받는사람, 날짜에 따른 메시지 데이터를 받아온다.
        // 2. 메시지를 시간 순으로 정렬한다.
        messageData msg1 = new messageData(1, 1, 2, "안녕하세요.","2022년 5월 8일 일요일", 11, 0);
        messageDataList.add(new messageData(2, 2, 1, "반갑습니다.", "2022년 5월 8일 일요일", 12, 10));
        messageDataList.add(msg1);
        messageDataList.add(new messageData(2, 2, 1, "반갑습니다.", "2022년 5월 8일 일요일", 12, 0));
        messageDataList.sort(new MessageTimeComparator());
        System.out.println("정렬 결과: ");
        for(messageData msg : messageDataList){
            System.out.println(msg.toString());
        }
    }
    public void addSendedMessage(String sendedText, String sendedDate){

        LinearLayout lin = new LinearLayout(context);
        lin.setLayoutParams(messageLayoutParams);
        lin.setOrientation(LinearLayout.HORIZONTAL);
        lin.setPadding(0, dp*10, 0, dp*10);
        lin.setGravity(Gravity.RIGHT);

        TextView message = new TextView(context);
        TextView date = new TextView(context);

        message.setText(sendedText);
        message.setLayoutParams(sendedMessageParams);
        message.setPadding(messagePadding, messagePadding, messagePadding, messagePadding);
        message.setBackgroundResource(R.drawable.rectangle);

        date.setText(sendedDate);
        date.setLayoutParams(dateParams);

        lin.addView(date);
        lin.addView(message);
        messageListLayout.addView(lin);
    }
    public void addReceivedMessage(String receivedText, String receivedDate){

        LinearLayout lin = new LinearLayout(context);
        lin.setLayoutParams(messageLayoutParams);
        lin.setOrientation(LinearLayout.HORIZONTAL);
        lin.setPadding(0, dp*10, 0, dp*10);
        lin.setGravity(Gravity.LEFT);

        TextView message = new TextView(context);
        TextView date = new TextView(context);

        message.setText(receivedText);
        message.setLayoutParams(receivedMessageParams);
        message.setPadding(messagePadding, messagePadding, messagePadding, messagePadding);
        message.setBackgroundResource(R.drawable.rectangle);

        date.setText(receivedDate);
        date.setLayoutParams(dateParams);

        lin.addView(message);
        lin.addView(date);
        messageListLayout.addView(lin);
    }

    public class MessageDateComparator implements Comparator<messageData>{
        @Override
        public int compare(messageData o1, messageData o2) {
            return o1.getDate().compareTo(o2.getDate());
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