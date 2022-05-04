package com.cookandroid.smartmirror.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.scheduleData;

public class ScheduleTypeDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private TextView AddOrEdit;
    private EditText titleEditText, startHourEditText, startMinuteEditText, endHourEditText, endMinuteEditText;
    private Button neagtiveBtn, positiveBtn;
    private String title;
    private int startHour, startMinute, endHour, endMinute;

    WindowManager windowManager;
    // 일정 추가라면 true, 일정 수정이라면 false
    private boolean isAdd= true;
    public ScheduleTypeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        this.isAdd = true;
    }
    public ScheduleTypeDialog(@NonNull Context context, scheduleData data){
        super(context);
        this.context = context;
        this.title = data.getTitle();
        this.startHour = data.getStartHour();
        this.startMinute = data.getStartMinute();
        this.endHour = data.getEndHour();
        this.endMinute = data.getEndMinute();
        this.isAdd = false;
    }
    ScheduleTypeDialog.ScheduleTypeDialogListener dlgListener;

    // 일정 추가/편집 다이얼로그 리스너
    public void setDlgListener(ScheduleTypeDialog.ScheduleTypeDialogListener listener){
        this.dlgListener = listener;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_schedule_edit);
//        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        titleEditText = findViewById(R.id.scheduleDialogTitleTextView);
        startHourEditText = findViewById(R.id.scheduleDialogStartHourEditText);
        startMinuteEditText = findViewById(R.id.scheduleDialogStartMinuteEditText);
        endHourEditText = findViewById(R.id.scheduleDialogEndHourEditText);
        endMinuteEditText = findViewById(R.id.scheduleDialogEndMinuteEditText);

        AddOrEdit = findViewById(R.id.scheduleDialogAddOrEdit);
        neagtiveBtn = findViewById(R.id.scheduleDialogNegativeBtn);
        positiveBtn = findViewById(R.id.scheduleDialogPositiveBtn);
        neagtiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("일정다이얼로그", "취소합니다.");
                dismiss();
            }
        });
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("일정다이얼로그", "저장합니다.");
                dismiss();
            }
        });

        if(isAdd){
            AddOrEdit.setText("일정추가");
        }else{
            // 편집 모드일 때에는 기존의 일정 정보를 갖고와서 세팅해둡니다.
            AddOrEdit.setText("일정수정");
            titleEditText.setText(title);
            startHourEditText.setText(Integer.toString(startHour));
            startMinuteEditText.setText(Integer.toString(startMinute));
            endHourEditText.setText(Integer.toString(endHour));
            endMinuteEditText.setText(Integer.toString(endMinute));

//            startTimePicker.setHour(startHour);
//            startTimePicker.setMinute(startMinute);
//            endTimePicker.setHour(endHour);
//            endTimePicker.setMinute(endMinute);

        }

    }

    @Override
    public void onClick(View v) {

    }

    public interface ScheduleTypeDialogListener{
        void onPositiveClicked(String title, int startHour, int startMinute, int endHour, int endMinute);
        void onNegariveClicked();
    }
}
