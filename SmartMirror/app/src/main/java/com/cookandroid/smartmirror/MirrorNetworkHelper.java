package com.cookandroid.smartmirror;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cookandroid.smartmirror.dataClass.devData;
import com.cookandroid.smartmirror.dataClass.interestedStockData;
import com.cookandroid.smartmirror.dataClass.layoutData;
import com.cookandroid.smartmirror.dataClass.messageData;
import com.cookandroid.smartmirror.dataClass.scheduleData;
import com.cookandroid.smartmirror.dataClass.userData;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
//import org.json.simple.JSONArray;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MirrorNetworkHelper {
    // Mirror Server와 통신을 도와주는 함수들
    public static int PORTNUMBER = 8000;
    public static String LOCATION = "Seoul";
    public static String INFO = "none";
    private String IP, Port;
//    public MirrorNetworkHelper(String IP, String Port){
//        this.IP = IP;
//        this.Port = Port;
//    }

    public devData getDevData(){
        // 시리얼넘버1, ip주소1
        devData newDevData = new devData("1", "1", PORTNUMBER, LOCATION, INFO);
        return newDevData;
    }
    public userData getUserData(){
        userData newUser = new userData(0, "1", "테스트", "\\bin");
        return newUser;
    }
    public messageData getMessageDate(){
        messageData msgData = new messageData(3, 2, 0, "안녕하쇼", "2022-05-27 12:30:00",true);
        return msgData;
    }
    public devData getDeviceData(){
        String deviceJson = "{"
                + "\"name\" : \"getDevice\", "
                + "\"deviceTable\" : ["
                + "{\"serial_no\": \"1\", \"ip\": \"1\", \"port\": \"5000\",\"location\":\"Seoul\",\"info\":\"정보없음\"}"
                + "]"
                + "}";
        try{
            JSONObject object = (JSONObject) new JSONTokener(deviceJson).nextValue();
            String name = object.getString("name");
            Log.i("jsonParsing", "name: "+name);
            org.json.JSONArray deviceTable = object.getJSONArray("deviceTable");
            JSONObject j = (JSONObject) deviceTable.get(0);
//                Log.i("jsonParsing", "row"+i+": "+j.toString());
            String serial_no = j.getString("serial_no");
            String ip = j.getString("ip");
            int port = j.getInt("port");
            String location = j.getString("location");
            String info = j.getString("info");
//                Log.i("jsonParsing", "user_noL "+user_no+", serial_no: "+serial_no+", userName: "+userName+", user_image_pass: "+user_image_pass);
            devData devData = new devData(serial_no, ip, port, location,info);
            return devData;
        }catch (Exception e) {
            e.printStackTrace();
        }

//            Log.i("jsonParsing", "user_no: "+deviceTable.getJSONObject(0).getInt("user_no"));

        return null;
    }

    public ArrayList<userData> getUserTable() {
        String deviceJson = "{"
                + "\"name\" : \"getDevice\", "
                + "\"userTable\" : ["
                + "{\"user_no\": 1, \"serial_no\": \"1\", \"name\": \"이진규\",\"user_image_pass\":\"\\lee\"},"
                + "{\"user_no\": 2, \"serial_no\": \"1\", \"name\": \"박동호\",\"user_image_pass\":\"\\park\"},"
                + "{\"user_no\": 3, \"serial_no\": \"1\", \"name\": \"전창민\",\"user_image_pass\":\"\\jeon\"}"
                + "]"
                + "}";

        try{
            ArrayList<userData> userDataList = new ArrayList<>();
            JSONObject object = (JSONObject) new JSONTokener(deviceJson).nextValue();
            String name = object.getString("name");
            Log.i("jsonParsing", "name: "+name);
            org.json.JSONArray deviceTable = object.getJSONArray("userTable");
            for(int i=0; i<deviceTable.length(); i++){
                JSONObject j = (JSONObject) deviceTable.get(i);
//                Log.i("jsonParsing", "row"+i+": "+j.toString());
                int user_no = j.getInt("user_no");
                String serial_no = j.getString("serial_no");
                String userName = j.getString("name");
                String user_image_pass = j.getString("user_image_pass");
//                Log.i("jsonParsing", "user_noL "+user_no+", serial_no: "+serial_no+", userName: "+userName+", user_image_pass: "+user_image_pass);
                userData newUser = new userData(user_no, serial_no, userName, user_image_pass);
                userDataList.add(newUser);
            }
            return userDataList;

//            Log.i("jsonParsing", "user_no: "+deviceTable.getJSONObject(0).getInt("user_no"));

        }catch (JSONException jsonException){
            jsonException.printStackTrace();
        }
        return null;
    }

    // 연결할 URL과 서버에 넘겨줄 JsonString으로 서버에 전송 후 받은 데이터를 String으로 Return해주는 함수
    public String connectionAndReturnString(String urlString, String jsonString) throws IOException {
        URL url = new URL(urlString.trim().toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //http 요청에 필요한 타입 정의 실시
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8"); //post body json으로 던지기 위함
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true); //OutputStream을 사용해서 post body 데이터 전송
        try (OutputStream os = conn.getOutputStream()) {
            byte request_data[] = jsonString.getBytes("utf-8");
            os.write(request_data);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //http 요청 실시
        conn.connect();
        Log.i("serverConnection", "요청주소: "+urlString+", 보낸데이터: "+jsonString);
        //http 요청 후 응답 받은 데이터를 버퍼에 쌓는다
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuffer sb = new StringBuffer();
        String responseData = "";
        while ((responseData = br.readLine()) != null) {
            sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
        }

        //메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입 실시
        //sb에는 서버로부터 받은 json 형식 데이터가 저장되어 있음, 문자열로 변환하여 사용
        String returnData = sb.toString();
        // 응답이 잘 왔는지 확인
        String responseCode = String.valueOf(conn.getResponseCode());
        Log.i("serverConnection","http 응답 코드 : " + responseCode+ "\nhttp 응답 데이터: "+returnData);
        return returnData;
    }

    public String addUserToServer(userData newUserData) {
        // 서버에 새 유저를 추가하고 받아온 ID로 sqlDB에도 추가합니다.
        String user_num="";
        try{

            JSONObject profileData = new JSONObject();
            profileData.put("name", newUserData.getName());
            profileData.put("serial_no", newUserData.getSerial_no());
            String postJsonString = profileData.toString();
            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/addProfile";

            String returnData = connectionAndReturnString(urlString, postJsonString);

            JSONObject object = (JSONObject) new JSONTokener(returnData).nextValue();
            user_num = object.getString("user_num");
            Log.i("jsonParsing", "user_num: " + user_num);
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("addUserToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("addUserToServer", "커넥션 오류");
        }finally {
            return user_num;
        }

    }
    public boolean editUserToServer(userData editUserData){
        try{

            JSONObject profileData = new JSONObject();
            profileData.put("user_num", editUserData.getUser_num());
            profileData.put("name", editUserData.getName());
            String postJsonString = profileData.toString();
            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/editProfile";

            String returnData = connectionAndReturnString(urlString, postJsonString);
            Log.i("jsonParsing", "ok | no: " + returnData);
            if(returnData.equals("ok")){
                return true;
            }
            
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("editUserToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("editUserToServer", "커넥션 오류");
        }
        return false;
    }
    public boolean delUserToServer(userData delUserData){
        try{

            JSONObject profileData = new JSONObject();
            profileData.put("user_num", delUserData.getUser_num());
            String postJsonString = profileData.toString();
            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/delProfile";

            String returnData = connectionAndReturnString(urlString, postJsonString);
            Log.i("jsonParsing", "ok | no: " + returnData);
            if(returnData.equals("ok")){
                return true;
            }

        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("delUserToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("delUserToServer", "커넥션 오류");
        }
        return false;
    }
    public ArrayList<layoutData> layoutSetFromServer(ArrayList<layoutData> layoutArrayList){
        JSONArray objectArray = null;
        ArrayList<layoutData> returnLayoutList=new ArrayList<>();
        try{

            JSONArray layoutDataArray = new JSONArray();
            for(layoutData l:layoutArrayList){
                JSONObject layoutData = new JSONObject();
                layoutData.put("user_num", l.getuser_num());
                layoutData.put("loc", l.getLoc());
                layoutData.put("type", l.getType());
                layoutDataArray.put(layoutData);
            }
            String postJsonString = layoutDataArray.toString();
            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/layoutSet";

            String returnData = connectionAndReturnString(urlString, postJsonString);
            objectArray = (JSONArray) new JSONTokener(returnData).nextValue();
            for(int i=0; i<objectArray.length(); i++){
                layoutData l = layoutArrayList.get(i);
                l.setLayout_id(objectArray.getJSONObject(i).getInt("layout_id"));
                returnLayoutList.add(l);
//                Log.i("layoutSetFromServer", "레이아웃: "+layoutArrayList.get(i).toString());
            }
//            Log.i("jsonParsing", "returnArrayList: " + returnLayoutList.toString());
            return returnLayoutList;
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("getAllTableFromServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("getAllTableFromServer", "커넥션 오류");
        }
        return returnLayoutList;
    }

    public String sendMessageToServer(messageData newMsgData){
        String message_id="";
        try{

            JSONObject messageData = new JSONObject();
            messageData.put("sender_num", newMsgData.getSender_num());
            messageData.put("user_num", newMsgData.getUser_num());
            messageData.put("text", newMsgData.getText());
            messageData.put("date", newMsgData.getDate());
            String postJsonString = messageData.toString();
            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/sendMessage";

            String returnData = connectionAndReturnString(urlString, postJsonString);

            JSONObject object = (JSONObject) new JSONTokener(returnData).nextValue();
            message_id = object.getString("message_id");
            Log.i("jsonParsing", "message_id: " + message_id);
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("addUserToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("addUserToServer", "커넥션 오류");
        }finally {
            return message_id;
        }
    }
    public boolean delMessageToServer(messageData delMessageData){
        try{

            JSONObject messageData = new JSONObject();
            messageData.put("message_id", delMessageData.getMessage_id());
            String postJsonString = messageData.toString();
            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/delMessage";

            String returnData = connectionAndReturnString(urlString, postJsonString);
            Log.i("jsonParsing", "ok | no: " + returnData);
            if(returnData.equals("ok")){
                return true;
            }

        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("delMessageToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("delMessageToServer", "커넥션 오류");
        }
        return false;
    }
    public String addScheduleToServer(scheduleData addScheduleData){
        String schedule_id="";
        try{

            JSONObject scheduleData = new JSONObject();
            scheduleData.put("user_num", addScheduleData.getUser_num());
            scheduleData.put("start_time", addScheduleData.getStartTime());
            scheduleData.put("end_time", addScheduleData.getEndTime());
            scheduleData.put("text", addScheduleData.getTitle());
            String postJsonString = scheduleData.toString();
            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/addSchedule";

            String returnData = connectionAndReturnString(urlString, postJsonString);

            JSONObject object = (JSONObject) new JSONTokener(returnData).nextValue();
            schedule_id = object.getString("schedule_id");
            Log.i("jsonParsing", "schedule_id: " + schedule_id);
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("addScheduleToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("addScheduleToServer", "커넥션 오류");
        }finally {
            return schedule_id;
        }
    }
    public boolean editScheduleToServer(scheduleData editScheduleData){
        try{

            JSONObject scheduleData = new JSONObject();
            scheduleData.put("schedule_id", editScheduleData.getSchedule_id());
            scheduleData.put("user_num", editScheduleData.getUser_num());
            scheduleData.put("start_time", editScheduleData.getStartTime());
            scheduleData.put("end_time", editScheduleData.getEndTime());
            scheduleData.put("text", editScheduleData.getTitle());
            String postJsonString = scheduleData.toString();
            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/editSchedule";

            String returnData = connectionAndReturnString(urlString, postJsonString);

//            JSONObject object = (JSONObject) new JSONTokener(returnData).nextValue();
            Log.i("jsonParsing", "okOrNO: " + returnData);
            if(returnData.contains("ok")){
                return true;
            }
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("editScheduleToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("editScheduleToServer", "커넥션 오류");
        }
        return false;
    }
    public boolean delScheduleToServer(scheduleData delSchedule){
        try{
            JSONObject scheduleData = new JSONObject();
            scheduleData.put("schedule_id", delSchedule.getUser_num());
            String postJsonString = scheduleData.toString();
            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/delSchedule";

            String returnData = connectionAndReturnString(urlString, postJsonString);

            Log.i("jsonParsing", "okOrNo: " + returnData);
            if(returnData.contains("ok")){
                return true;
            }
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("delScheduleToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("delScheduleToServer", "커넥션 오류");
        }
        return false;
    }

    public String addStockToServer(interestedStockData addStockData){
        String stock_id="";
        try{

            JSONObject stockData = new JSONObject();
            stockData.put("user_num", addStockData.getUser_num());
            stockData.put("stock_code", addStockData.getStock_code());
            stockData.put("stock_name", addStockData.getStock_name());
            String postJsonString = stockData.toString();
            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/addStock";

            String returnData = connectionAndReturnString(urlString, postJsonString);

            JSONObject object = (JSONObject) new JSONTokener(returnData).nextValue();
            stock_id = object.getString("stock_id");
            Log.i("jsonParsing", "stock_id: " + stock_id);
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("addScheduleToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("addScheduleToServer", "커넥션 오류");
        }finally {
            return stock_id;
        }
    }
    public boolean delStockToServer(interestedStockData delStockData){
        try{
            JSONObject stockData = new JSONObject();
            stockData.put("stock_id", delStockData.getStock_id());
            String postJsonString = stockData.toString();
            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/delStock";

            String returnData = connectionAndReturnString(urlString, postJsonString);

            Log.i("jsonParsing", "okOrNo: " + returnData);
            if(returnData.contains("ok")){
                return true;
            }
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("delStockToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("delStockToServer", "커넥션 오류");
        }
        return false;
    }
    public JSONObject getAllTableFromServer(SQLiteDatabase db){
        JSONObject object = null;
        try{

            JSONObject profileData = new JSONObject();
            profileData.put("serial_no", "1");
            String postJsonString = profileData.toString();
            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/syncAllTable";

            String returnData = connectionAndReturnString(urlString, postJsonString);

            object = (JSONObject) new JSONTokener(returnData).nextValue();
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("getAllTableFromServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("getAllTableFromServer", "커넥션 오류");
        }finally {
            return object;
        }
    }


    public void httpMain() throws JSONException {
        System.out.println("[HttpURLConnection 사용해  post body json 방식 데이터 요청 및 응답 값 확인 실시]");

        /*[설 명]
         * 1. HttpURLConnection은 http 통신을 수행할 객체입니다
         * 2. URL 객체로 connection을 만듭니다
         * 3. 응답받은 결과를 InputStream으로 받아서 버퍼에 순차적으로 쌓습니다
         * */

        //데이터 정의 실시
        //포트 넘버뒤에 오는 URL을 통해 플라스크 서버에게 요청
        String url = "http://192.168.0.6:8000/getData";

        //하나하나 줄바꾸면서 쓰기 너무 어려워보임
        //String data2 = "{ \"userId\" : \"1\", \"id\" : \"1\" }"; //json 형식 데이터




        // JSONObject jsonObject = new JSONObject();

        // jsonObject.put("SECR_KEY", "ktko.tistory.com");
        // jsonObject.put("KEY", "ktko");

        //json 객체 생성 및 데이터 입력
//        org.json.simple.JSONObject data1 = new org.json.simple.JSONObject();
        JSONObject data1 = new JSONObject();
        data1.put("name", "이진규");
        data1.put("serial_no", "1");

//        org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
        JSONObject data2 = new JSONObject();
        data2.put("BANK_CD", "088");
        data2.put("SEARCH_ACCT_NO", "1231231234");
        data2.put("ACNM_NO", "123456");
        data2.put("ICHE_AMT", "0");
        data2.put("TRSC_SEQ_NO", "0000001");

        //json 배열 객체 생성
        JSONArray req_array = new JSONArray();
        //json 어레이에 두가지 데이터 추가
        req_array.put(data1);
        req_array.put(data2);

        // jsonObject.put("REQ_DATA", req_array);

        //json 형식 string 으로 변환
        String data3 = data1.toString();

        //data3 은 POST 방식을 이용해 플라스크 서버로 전송
        //메소드 호출 실시
        httpPostBodyConnection(url, data3);
    }
    public void httpPostBodyConnection(String UrlData, String ParamData) {

        //http 요청 시 필요한 url 주소를 변수 선언
        String totalUrl = "";
        totalUrl = UrlData.trim().toString();

        //http 통신을 하기위한 객체 선언 실시
        URL url = null;
        HttpURLConnection conn = null;

        //http 통신 요청 후 응답 받은 데이터를 담기 위한 변수
        String responseData = "";
        BufferedReader br = null;
        StringBuffer sb = null;

        //메소드 호출 결과값을 반환하기 위한 변수
        String returnData = "";

        try {
            //파라미터로 들어온 url을 사용해 connection 실시
            url = new URL(totalUrl);
            conn = (HttpURLConnection) url.openConnection();

            //http 요청에 필요한 타입 정의 실시
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8"); //post body json으로 던지기 위함
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); //OutputStream을 사용해서 post body 데이터 전송
            try (OutputStream os = conn.getOutputStream()) {
                byte request_data[] = ParamData.getBytes("utf-8");
                os.write(request_data);
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //http 요청 실시
            conn.connect();
            Log.i("serverConnection", "요청주소: "+UrlData+", 보낸데이터: "+ParamData);

            //http 요청 후 응답 받은 데이터를 버퍼에 쌓는다
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            sb = new StringBuffer();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
            }

            //메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입 실시
            //sb에는 서버로부터 받은 json 형식 데이터가 저장되어 있음, 문자열로 변환하여 사용
            returnData = sb.toString();

            //http 요청 응답 코드 확인 실시
            String responseCode = String.valueOf(conn.getResponseCode());
            System.out.println("http 응답 코드 : " + responseCode);
            System.out.println("http 응답 데이터 : " + returnData);

            try {
                JSONObject object = (JSONObject) new JSONTokener(returnData).nextValue();
                String user_num = object.getString("user_num");
                Log.i("jsonParsing", "user_num: " + user_num);
//                JSONArray deviceTable = object.getJSONArray("deviceTable");
//                JSONObject j = (JSONObject) deviceTable.get(0);
////                Log.i("jsonParsing", "row"+i+": "+j.toString());
//                String serial_no = j.getString("serial_no");
//                String ip = j.getString("ip");
//                int port = j.getInt("port");
//                String location = j.getString("location");
//                String info = j.getString("info");
//                Log.i("jsonParsing", "user_noL "+user_no+", serial_no: "+serial_no+", userName: "+userName+", user_image_pass: "+user_image_pass);
//                userData newUser = new userData(user_num, );
//                devData devData = new devData(serial_no, ip, port, location, info);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            //json 데이터 다루기, 파싱 작동 테스트
//            JSONParser parser = new JSONParser();
//            JSONTokener tokener = new JSONTokener();
//            //json 서버에서 응답받은 데이터 어레이 객체로 파싱
//            JSONArray arr = null;
//            try {
//                arr = (JSONArray)parser.parse(returnData);
//            } catch (ParseException e) {
//                System.out.println("변환에 실패");
//                e.printStackTrace();
//            }
//
//            //파싱한 json 배열 크기만큼 반복, json 배열을 json 단일 객채로 파싱하고 데이터 사용
//            System.out.println(arr.size());
//            for(int i = 0; i<arr.size(); i++){
//                System.out.println(arr.get(i));
//                org.json.simple.JSONObject jObject = null;
//                try {
//                    jObject = (org.json.simple.JSONObject)parser.parse(arr.get(i).toString());
//                } catch (ParseException e) {
//                    System.out.println("변환에 실패");
//                    e.printStackTrace();
//                }
//                //json 객체 작동 테스트
//                System.out.println("i:"+ jObject.get("i"));
//                System.out.println("j:"+ jObject.get("j"));
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            //http 요청 및 응답 완료 후 BufferedReader를 닫아줍니다
//            try {
//                if (br != null) {
//                    br.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //http 요청 및 응답 완료 후 BufferedReader, conn을 닫아줍니다
            conn.disconnect();
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
