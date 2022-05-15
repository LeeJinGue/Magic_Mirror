package com.cookandroid.smartmirror.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.scheduleData;

public class ScheduleTypeDialog extends DialogFragment implements View.OnClickListener{
    private Context context;
    private TextView AddOrEdit;
    private EditText titleEditText, startHourEditText, startMinuteEditText, endHourEditText, endMinuteEditText;
    private Button neagtiveBtn, positiveBtn;
    private String title;
    private int startHour, startMinute, endHour, endMinute, iconRes, index;
    private scheduleData data;
    WindowManager windowManager;
    // 일정 추가라면 true, 일정 수정이라면 false
    private boolean isAdd;

    public ScheduleTypeDialog() {

    }
    public interface EditScheduleDialogListener{
        void onFinishedEditDialog(int index, scheduleData data);
        void onFinishedAddDialog(scheduleData data);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        isAdd = getArguments().getBoolean("isAdd");
        data = getArguments().getParcelable("Data");

//        iconRes = getArguments().getInt("iconRes");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_schedule_edit, null);
        titleEditText = view.findViewById(R.id.scheduleDialogTitleTextView);
        startHourEditText = view.findViewById(R.id.scheduleDialogStartHourEditText);
        startMinuteEditText = view.findViewById(R.id.scheduleDialogStartMinuteEditText);
        endHourEditText = view.findViewById(R.id.scheduleDialogEndHourEditText);
        endMinuteEditText = view.findViewById(R.id.scheduleDialogEndMinuteEditText);
//        AddOrEdit = view.findViewById(R.id.scheduleDialogAddOrEdit);
        isAdd = getArguments().getBoolean("isAdd");

        if(isAdd){
            alertDialogBuilder.setTitle("일정추가");
        }else{
            alertDialogBuilder.setTitle("일정수정");
            titleEditText.setText(data.getTitle());
            startHourEditText.setText(Integer.toString(data.getStartHour()));
            startMinuteEditText.setText(Integer.toString(data.getStartMinute()));
            endHourEditText.setText(Integer.toString(data.getEndHour()));
            endMinuteEditText.setText(Integer.toString(data.getEndMinute()));
            index = getArguments().getInt("index");
        }
        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("ScheduleTypeDialog", "확인버튼 클릭");
                EditScheduleDialogListener listener = (EditScheduleDialogListener) getParentFragment();

                scheduleData newData = new scheduleData(titleEditText.getText().toString()
                        , -1
                        , Integer.parseInt(startHourEditText.getText().toString())
                        , Integer.parseInt(startMinuteEditText.getText().toString())
                        , Integer.parseInt(endHourEditText.getText().toString())
                        , Integer.parseInt(endMinuteEditText.getText().toString()));
                if(isAdd) {
                    // 일정 추가일 때
                    newData.setIconRes(R.drawable.ic_schedule_blue);
                    listener.onFinishedAddDialog(newData);
                    dialog.dismiss();
                }else{
                    // 일정 수정일 때
                    newData.setIconRes(data.getIconRes());
                    listener.onFinishedEditDialog(index, newData);
                    dialog.dismiss();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("ScheduleTypeDialog", "취소버튼 클릭");
                dialog.dismiss();
            }
        });
        return alertDialogBuilder.create();
    }

    public static ScheduleTypeDialog newInstance(scheduleData data, int index) {
        ScheduleTypeDialog dialogFrag = new ScheduleTypeDialog();
        Bundle args = new Bundle();
        if (data == null) {
            args.putBoolean("isAdd", true);
        } else {
            Log.i("ScheduleTypeDialog", "전달받은 data: "+data.toString());
            args.putInt("index", index);
            args.putBoolean("isAdd", false);
            args.putParcelable("Data", data);
        }

        dialogFrag.setArguments(args);
        return dialogFrag;
    }
    ScheduleTypeDialog.ScheduleTypeDialogListener dlgListener;

    // 일정 추가/편집 다이얼로그 리스너
    public void setDlgListener(ScheduleTypeDialog.ScheduleTypeDialogListener listener){
        this.dlgListener = listener;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_schedule_edit, container);
    }


    @Override
    public void onClick(View v) {

    }

    public interface ScheduleTypeDialogListener{
        void onPositiveClicked(String title, int startHour, int startMinute, int endHour, int endMinute);
        void onNegariveClicked();
    }
}
