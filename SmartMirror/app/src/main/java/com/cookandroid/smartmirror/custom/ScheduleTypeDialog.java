package com.cookandroid.smartmirror.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.cookandroid.smartmirror.R;

public class ScheduleTypeDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private TextView tvTitle;
    private Button neagtiveBtn, positiveBtn;
    private int startHour, startMinute, endHour, endMinute;
    private TimePicker startTimePicker, endTimePicker;
    public ScheduleTypeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
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
        tvTitle = findViewById(R.id.scheduleDialogTitle);
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
        startTimePicker = findViewById(R.id.scheduleDialogStartTime);
        startTimePicker.setHour(startHour);
        startTimePicker.setMinute(startMinute);
        endTimePicker = findViewById(R.id.scheduleDialogEndTime);
        endTimePicker.setHour(endHour);
        endTimePicker.setMinute(endMinute);

    }

    @Override
    public void onClick(View v) {

    }

    public interface ScheduleTypeDialogListener{
        void onPositiveClicked(String title, int startHour, int startMinute, int endHour, int endMinute);
        void onNegariveClicked();
    }
}
