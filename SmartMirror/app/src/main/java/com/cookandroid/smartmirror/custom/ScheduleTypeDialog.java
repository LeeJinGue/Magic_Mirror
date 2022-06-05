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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.scheduleData;
import com.cookandroid.smartmirror.dataClass.userData;

import java.time.LocalDate;

public class ScheduleTypeDialog extends DialogFragment implements DialogInterface.OnClickListener{
    private Context context;
    private TextView AddOrEdit;
    private EditText titleEditText, startHourEditText, startMinuteEditText, endHourEditText, endMinuteEditText;
    private Button neagtiveBtn, positiveBtn;
    private String title, selectedDate;
    private int startHour, startMinute, endHour, endMinute, iconRes, index;
    private scheduleData data;
    private userData selectedUser;
    WindowManager windowManager;
    View view;
    // 일정 추가라면 true, 일정 수정이라면 false
    private boolean isAdd;
    MyApplication myApp;
    public ScheduleTypeDialog(MyApplication myApp) { this.myApp = myApp;
    selectedUser = myApp.getSelectedUser();}

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case AlertDialog.BUTTON_POSITIVE:
                Log.i("ScheduleTypeDialog", "확인버튼 클릭");
                EditScheduleDialogListener listener = (EditScheduleDialogListener) getParentFragment();
                // 스케줄아이디, 유저아이디, 시작시간, 종료시간, 제목, 아이콘
//                LocalDate nowDate = LocalDate.now();
                String date = Methods.getNowDate();
//                String dateTime = Methods.getNowDateTime();
                // 시:분:초. 초까지 붙여줘야 한다.
                String startHourString = Methods.checkHour(startHourEditText.getText().toString());
                String startMinuteString = Methods.checkMinute(startMinuteEditText.getText().toString());
                String endHourString = Methods.checkHour(endHourEditText.getText().toString());
                String endMinuteString = Methods.checkMinute(endMinuteEditText.getText().toString());
                if(startHourString.equals("") || startHourString.equals("over") || startMinuteString.equals("") || startMinuteString.equals("over")){
                    Toast.makeText(getActivity(), "시작시간을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    Log.i("ScheduleTypeDialog", "시작시간 오류");
                    return;
                }else if(endHourString.equals("") || endHourString.equals("over") || endMinuteString.equals("") || endMinuteString.equals("over")){
                    Toast.makeText(getActivity(), "종료시간을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    Log.i("ScheduleTypeDialog", "종료시간 오류");
                    return;
                }else{
                    // 시간이 똑바로 되어있다면 저장
                    String startTime = startHourString+":"+startMinuteString+":00";
                    String startDateTime = date + " " +startTime;
                    String endTime = endHourString+":"+endMinuteString+":00";
                    String endDateTime = date + " " + endTime;
                    scheduleData newData = new scheduleData(
                            1, selectedUser.getUser_num()
                            , startDateTime, endDateTime,
                            titleEditText.getText().toString());
                    if(isAdd) {
                        // 일정 추가일 때
                        newData.setIconRes(R.drawable.ic_schedule_blue);
                        listener.onFinishedAddDialog(newData);
                        dialog.dismiss();
                    }else{
                        // 일정 수정일 때
                        newData.setIconRes(data.getIconRes());
                        newData.setSchedule_id(data.getSchedule_id());
                        listener.onFinishedEditDialog(index, newData);
                        dialog.dismiss();
                    }
                }
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                Log.i("ScheduleTypeDialog", "취소버튼 클릭");
                dialog.dismiss();
                break;
            default:
                break;
        }
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
        context = getActivity().getApplicationContext();
        selectedDate = getArguments().getString("selectedDate");
//        selectedUser = myApp.getSelectedUser();
//        iconRes = getArguments().getInt("iconRes");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        view = getLayoutInflater().inflate(R.layout.dialog_schedule_edit, null);
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
            startHourEditText.setText(data.getStartHour());
            startMinuteEditText.setText(data.getStartMinute());
            endHourEditText.setText(data.getEndHour());
            endMinuteEditText.setText(data.getEndMinute());
            selectedDate = data.getDate();
            index = getArguments().getInt("index");
        }
        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("ScheduleTypeDialog", "확인버튼 클릭");
                EditScheduleDialogListener listener = (EditScheduleDialogListener) getParentFragment();
                // 스케줄아이디, 유저아이디, 시작시간, 종료시간, 제목, 아이콘
//                LocalDate nowDate = LocalDate.now();

                String date = Methods.getNowDate();
//                String dateTime = Methods.getNowDateTime();
                // 시:분:초. 초까지 붙여줘야 한다.
                String startHourString = Methods.checkHour(startHourEditText.getText().toString());
                String startMinuteString = Methods.checkMinute(startMinuteEditText.getText().toString());
                String endHourString = Methods.checkHour(endHourEditText.getText().toString());
                String endMinuteString = Methods.checkMinute(endMinuteEditText.getText().toString());
                Log.i("ScheduleTypeDialog", "중간체크\n"+"startHourString: "+startHourString+", startMinuteString: "+startMinuteString+", endHourString: "+endHourString+", endMinuteString: "+endMinuteString);
                if(startHourString.equals("") || startHourString.equals("over") || startMinuteString.equals("") || startMinuteString.equals("over")){
                    Toast.makeText(getActivity(), "시작시간을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    Log.i("ScheduleTypeDialog", "시작시간 오류");
                    return;
                }else if(endHourString.equals("") || endHourString.equals("over") || endMinuteString.equals("") || endMinuteString.equals("over")){
                    Toast.makeText(getActivity(), "종료시간을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    Log.i("ScheduleTypeDialog", "종료시간 오류");
                    return;
                }else{
                    // 시간이 똑바로 되어있다면 저장
                    String startTime = startHourString+":"+startMinuteString+":00";
                    String endTime = endHourString+":"+endMinuteString+":00";
                    String startDateTime = selectedDate + " " +startTime;
                    String endDateTime = selectedDate + " " + endTime;
                    scheduleData newData = new scheduleData(
                            1, selectedUser.getUser_num()
                            , startDateTime, endDateTime,
                            titleEditText.getText().toString());
                    if(isAdd) {
                        // 일정 추가일 때
                        newData.setIconRes(R.drawable.ic_schedule_blue);
                        listener.onFinishedAddDialog(newData);
                        dialog.dismiss();
                    }else{
                        // 일정 수정일 때
                        newData.setIconRes(data.getIconRes());
                        newData.setSchedule_id(data.getSchedule_id());
                        listener.onFinishedEditDialog(index, newData);
                        dialog.dismiss();
                    }
                }
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("ScheduleTypeDialog", "취소버튼 클릭");
                dialog.dismiss();
            }
        });
        return alertDialogBuilder.create();
    }

    public static ScheduleTypeDialog newInstance(scheduleData data, int index, MyApplication myApp, String selectedDate) {
        ScheduleTypeDialog dialogFrag = new ScheduleTypeDialog(myApp);
        Bundle args = new Bundle();
        if (data == null) {
            Log.i("ScheduleTypeDialog", "선택된 날짜: "+selectedDate);
            args.putBoolean("isAdd", true);
            args.putString("selectedDate", selectedDate);
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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_schedule_edit, container);
    }




    public interface ScheduleTypeDialogListener{
        void onPositiveClicked(String title, int startHour, int startMinute, int endHour, int endMinute);
        void onNegariveClicked();
    }
}
