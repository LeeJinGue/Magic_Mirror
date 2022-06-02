package com.cookandroid.smartmirror;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Methods {
// Set AppBar Title Gradient Color
    public void setGradient(int startColor, int endColor, TextView tv){

        Shader textShader = new LinearGradient(0, 0, tv.getPaint().measureText(tv.getText().toString()), tv.getTextSize(),
                new int[]{startColor, endColor},
                new float[]{0,1}, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(textShader);
    }
    // onclickListner에서 index를 전달하기 위한 함수

    public static int ConvertDPtoPX(Context context, int dp) { float density = context.getResources().getDisplayMetrics().density; return Math.round((float) dp * density); }
    public static void JsonTest() throws JSONException {
        JSONObject json1 = new JSONObject();
        json1.put("name", "syncAllTable");

    }
    // ---------------------- 날짜관리 --------------------------------
    // DB에서 받아온 String형식의 DateTime 데이터를 통해 년/월/일/시/분 값을 가져옵니다.
    public static String getYearFromDateString(String time){
        return time.substring(0,4);
    }
    public static String getMonthFromDateString(String time){
        return time.substring(5,7);
    }
    public static String getDayFromDateString(String time){
        return time.substring(8,10);
    }
    public static String getDateFromDateString(String time){
        return time.substring(0,10);
    }
    public static String getHourFromDateString(String time){
        return time.substring(11, 13);
    }
    public static String getMinuteFromDateString(String time){
        return time.substring(14, 16);
    }

    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // String(yyyy-mm-dd) -> date로 변환
    public static Date getConvertStringToDate(String dateString){
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try{
            date = format.parse(dateString);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }
    // yyyy-mm-dd 형식의 Date에 1일을 더한 값을 리턴 -> Schedule쪽에 쓰임.
    public static String getDateStringFromInteger(int year, int month, int day){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.of(year, month, day).format(dtf);
    }
    public static String getNextDate(String currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getConvertStringToDate(currentDate));
        cal.add(cal.DATE, +1);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(cal.getTime());
    }
    public static String getNowDateTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.now().format(dtf);
    }
    public static String getNowDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.now().format(dtf);
    }
    public static String checkHour(String hourString){
        if(isInteger(hourString)){
            int hourInteger = Integer.parseInt(hourString);
            if(hourInteger >= 0 && hourInteger < 10){
                // 0 - 9라면 앞에 0을 붙여줍니다.
                return "0"+hourInteger;
            }else if(hourInteger < 24){
                // 아니고 24 미만이라면 받은걸 그대로 리턴합니다.
                // 023 이런식으로 되어있을 수도 있으니 Integer로 한번 Parsing하고 다시 String으로 리턴합니다.
                return String.valueOf(hourInteger);
            }else{
                // 24이상이면 over를 리턴합니다.
                return "over";
            }
        }else{
            // 정수가 아니라면 공백을 리턴합니다.
            return "";
        }
    }
    public static String checkMinute(String minuteString){
        if(isInteger(minuteString)){
            int minuteInteger = Integer.parseInt(minuteString);
            if(minuteInteger >= 0 && minuteInteger < 10){
                // 0 - 9라면 앞에 0을 붙여줍니다.
                return "0"+ minuteInteger;
            }else if(minuteInteger < 60){
                // 아니고 24 미만이라면 받은걸 그대로 리턴합니다.
                // 059 이런식으로 되어있을 수도 있으니 Integer로 한번 Parsing하고 다시 String으로 리턴합니다.
                return String.valueOf(minuteInteger);
            }else{
                // 60보다 크다면 over를 리턴합니다.
                return "over";
            }
        }else{
            // 정수가 아니라면 공백을 리턴합니다.
            return "";
        }
    }

    public static boolean isInteger(String strNum){
        if(strNum == null){
            return false;
        }
//        else if(strNum.length() >= 3){
//            return false;
//        }
        try{
            int i = Integer.parseInt(strNum);
        }catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }
    // -------------------------------------------------------------

    // ----------------------stuff_list <--> ArrayList<String>---------------------
    public static ArrayList<String> getStuffArrayListFromString(String stuff_list){
        // ,로 stuff_list를 쪼갭니다.
        String[] stringArray = stuff_list.split(",");
        ArrayList<String> stuffList_arr = new ArrayList<String>(Arrays.asList(stringArray));
        Log.i("getStuffArrayListFromString", "stuffList_arr: "+stuffList_arr.toString());
        return stuffList_arr;
    }
    public static String getStringFromStuffArrayList(ArrayList<String> stuffList_arr){
        // ArrayList를 toString하면 item사이에 ", "가 붙는데, 여기에서 붙는 공백을 제거해줍니다. 앞뒤에 붙는 []도 제거해줍니다.
        String stuff_list = stuffList_arr.toString().replace(", ", ",").replace("[","").replace("]","");

//        for(String s:stuffList_arr) stuff_list += ","+s;
        Log.i("getStringFromStuffArrayList", "stuff_list: "+stuff_list);
        return stuff_list;
    }
    // -----------------------------------------------------------------------------

}
