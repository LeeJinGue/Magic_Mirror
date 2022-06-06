package com.cookandroid.smartmirror.helper;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cookandroid.smartmirror.dataClass.belongingSetData;
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
import java.net.URL;
import java.util.ArrayList;

public class MirrorNetworkHelper {
    // Mirror Server와 통신을 도와주는 함수들
    public static int PORTNUMBER = 8000;
    public static String LOCATION = "Seoul";
    public static String INFO = "none";
    private String IPString, PortString;
    public MirrorNetworkHelper(String IPString, String PortStrng){
        this.IPString = IPString;
        this.PortString = PortStrng;
    }

    public void setIPString(String IPString) {
        this.IPString = IPString;
        this.PortString= MethodsHelper.DEFAULT_PORT;
    }

    // 연결할 URL과 서버에 넘겨줄 JsonString으로 서버에 전송 후 받은 데이터를 String으로 Return해주는 함수
    public String connectionAndReturnString(String funcString, String jsonString) throws IOException {
        String urlString = "http://"+IPString+":"+PortString+funcString;
        Log.i("connectionAndReturnString", "연결주소: "+urlString);
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
            conn.disconnect();
            return "";
        }

        try{

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
            conn.disconnect();
            Log.i("serverConnection","http 응답 코드 : " + responseCode+ "\nhttp 응답 데이터: "+returnData);
            return returnData;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    //------------------------- 유저 테이블 --------------------------
    public String addUserToServer(userData newUserData) {
        // 서버에 새 유저를 추가하고 받아온 ID로 sqlDB에도 추가합니다.
        String user_num="";
        try{

            JSONObject profileData = new JSONObject();
            profileData.put("name", newUserData.getName());
            profileData.put("serial_no", newUserData.getSerial_no());
            String postJsonString = profileData.toString();
//            String urlString = "http://"+"192.168.1.101"+":"+"8000"+"/addProfile";
            String funcString = "/addProfile";
            String returnData = connectionAndReturnString(funcString, postJsonString);

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
            String funcString = "/editProfile";
            String returnData = connectionAndReturnString(funcString, postJsonString);
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
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/delProfile";
            String funcString = "/delProfile";
            String returnData = connectionAndReturnString(funcString, postJsonString);
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
    //---------------------------------------------------------------

    //------------------------- 화면설정 테이블 --------------------------
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
//            String urlString = "http://"+"192.168.1.101"+":"+"8000"+"/layoutSet";
            String funcString = "/layoutSet";
            String returnData = connectionAndReturnString(funcString, postJsonString);
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
    //---------------------------------------------------------------

    //------------------------- 메시지 테이블 --------------------------
    public String sendMessageToServer(messageData newMsgData){
        String message_id="";
        try{

            JSONObject messageData = new JSONObject();
            messageData.put("sender_num", newMsgData.getSender_num());
            messageData.put("user_num", newMsgData.getUser_num());
            messageData.put("text", newMsgData.getText());
            messageData.put("date", newMsgData.getDate());
            String postJsonString = messageData.toString();
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/sendMessage";
            String funcString = "/sendMessage";
            String returnData = connectionAndReturnString(funcString, postJsonString);

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
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/delMessage";
            String funcString = "/delMessage";
            String returnData = connectionAndReturnString(funcString, postJsonString);
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
    //---------------------------------------------------------------

    //------------------------- 일정 테이블 --------------------------
    public String addScheduleToServer(scheduleData addScheduleData){
        String schedule_id="";
        try{

            JSONObject scheduleData = new JSONObject();
            scheduleData.put("user_num", addScheduleData.getUser_num());
            scheduleData.put("start_time", addScheduleData.getStartTime());
            scheduleData.put("end_time", addScheduleData.getEndTime());
            scheduleData.put("text", addScheduleData.getTitle());
            String postJsonString = scheduleData.toString();
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/addSchedule";
            String funcString = "/addSchedule";
            String returnData = connectionAndReturnString(funcString, postJsonString);

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
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/editSchedule";
            String funcString = "/editSchedule";
            String returnData = connectionAndReturnString(funcString, postJsonString);

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
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/delSchedule";
            String funcString = "/delSchedule";
            String returnData = connectionAndReturnString(funcString, postJsonString);

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
    //---------------------------------------------------------------

    //------------------------- 관심주식 테이블 --------------------------
    public String addStockToServer(interestedStockData addStockData){
        String stock_id="";
        try{

            JSONObject stockData = new JSONObject();
            stockData.put("user_num", addStockData.getUser_num());
            stockData.put("stock_code", addStockData.getStock_code());
            stockData.put("stock_name", addStockData.getStock_name());
            String postJsonString = stockData.toString();
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/addStock";
            String funcString = "/addStock";
            String returnData = connectionAndReturnString(funcString, postJsonString);

            JSONObject object = (JSONObject) new JSONTokener(returnData).nextValue();
            stock_id = object.getString("stock_id");
            Log.i("jsonParsing", "stock_id: " + stock_id);
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("addScheduleToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("addScheduleToServer", "커넥션 오류");
        }
        return stock_id;

    }
    public boolean delStockToServer(interestedStockData delStockData){
        try{
            JSONObject stockData = new JSONObject();
            stockData.put("stock_id", delStockData.getStock_id());
            String postJsonString = stockData.toString();
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/delStock";
            String funcString = "/delStock";

            String returnData = connectionAndReturnString(funcString, postJsonString);

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
    //---------------------------------------------------------------

    //------------------------- 소지품 테이블 --------------------------
    public String addBelongingSetToServer(belongingSetData addBelongingSetData){
        String belonging_id="";
        try{
            JSONObject belongingSetData = new JSONObject();
            belongingSetData.put("user_num", addBelongingSetData.getUser_num());
            belongingSetData.put("set_name", addBelongingSetData.getSet_name());
            belongingSetData.put("set_info", addBelongingSetData.getSet_info());
            belongingSetData.put("stuff_list", addBelongingSetData.getStuff_list_str());
            belongingSetData.put("activation", addBelongingSetData.getActivation());

            String postJsonString = belongingSetData.toString();
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/addBelongingSet";
            String funcString = "/addBelongingSet";

            String returnData = connectionAndReturnString(funcString, postJsonString);

            JSONObject object = (JSONObject) new JSONTokener(returnData).nextValue();
            belonging_id = object.getString("belonging_id");
            Log.i("jsonParsing", "belonging_id: " + belonging_id);
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("addBelongingSetToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("addBelongingSetToServer", "커넥션 오류");
        }
        return belonging_id;
    }
    public boolean editBelongingSetToServer(belongingSetData editBelongingSetData){
        try{
            JSONObject belongingSetData = new JSONObject();
            belongingSetData.put("belonging_id", editBelongingSetData.getBelonging_id());
            belongingSetData.put("set_name", editBelongingSetData.getSet_name());
            belongingSetData.put("set_info", editBelongingSetData.getSet_info());
            belongingSetData.put("stuff_list", editBelongingSetData.getStuff_list_str());
            belongingSetData.put("activation", editBelongingSetData.getActivation());
            String postJsonString = belongingSetData.toString();
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/editBelongingSet";
            String funcString = "/editBelongingSet";

            String returnData = connectionAndReturnString(funcString, postJsonString);

//            JSONObject object = (JSONObject) new JSONTokener(returnData).nextValue();
            Log.i("jsonParsing", "okOrNO: " + returnData);
            if(returnData.contains("ok")){
                return true;
            }
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("editBelongingSetToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("editBelongingSetToServer", "커넥션 오류");
        }
        return false;
    }
    public boolean activationBelongingSetToServer(belongingSetData activationBelongingSetData){
        try{
            JSONObject belongingSetData = new JSONObject();
            belongingSetData.put("user_num", activationBelongingSetData.getUser_num());
            belongingSetData.put("belonging_id", activationBelongingSetData.getBelonging_id());
            String postJsonString = belongingSetData.toString();
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/activationBelongingSet";
            String funcString = "/activationBelongingSet";

            String returnData = connectionAndReturnString(funcString, postJsonString);

//            JSONObject object = (JSONObject) new JSONTokener(returnData).nextValue();
            Log.i("jsonParsing", "okOrNO: " + returnData);
            if(returnData.contains("ok")){
                return true;
            }
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("editBelongingSetToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("editBelongingSetToServer", "커넥션 오류");
        }
        return false;
    }
    public boolean deactivationBelongingSetToServer(belongingSetData deactivationBelongingSetData){
        try{
            JSONObject belongingSetData = new JSONObject();
            belongingSetData.put("belonging_id", deactivationBelongingSetData.getBelonging_id());
            String postJsonString = belongingSetData.toString();
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/activationBelongingSet";
            String funcString = "/deactivationBelongingSet";

            String returnData = connectionAndReturnString(funcString, postJsonString);
            Log.i("jsonParsing", "okOrNO: " + returnData);
            if(returnData.contains("ok")){
                return true;
            }
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("deactivationBelongingSetToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("deactivationBelongingSetToServer", "커넥션 오류");
        }
        return false;
    }
    public boolean delBelongingSetToServer(belongingSetData delBelongingSetData){
        try{
            JSONObject belongingSetData = new JSONObject();
            belongingSetData.put("belonging_id", delBelongingSetData.getBelonging_id());
            String postJsonString = belongingSetData.toString();
//            String urlString = "http://"+"192.168.0.6"+":"+"8000"+"/delBelongingSet";
            String funcString = "/delBelongingSet";

            String returnData = connectionAndReturnString(funcString, postJsonString);

//            JSONObject object = (JSONObject) new JSONTokener(returnData).nextValue();
            Log.i("jsonParsing", "okOrNO: " + returnData);
            if(returnData.contains("ok")){
                return true;
            }
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("delBelongingSetToServer", "Json파싱오류");
        }catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("delBelongingSetToServer", "커넥션 오류");
        }
        return false;
    }
    //---------------------------------------------------------------

    //-------------------------전체 테이블 받아오기 --------------------------
    public JSONObject getAllTableFromServer(SQLiteDatabase db){
        JSONObject object = null;
        try{

            JSONObject profileData = new JSONObject();
            profileData.put("serial_no", "1");
            String postJsonString = profileData.toString();
//            String urlString = "http://"+"192.168.1.101"+":"+"8000"+"/syncAllTable";
            String funcString = "/syncAllTable";

            String returnData = connectionAndReturnString(funcString, postJsonString);
            if(returnData=="") return null;
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
    //---------------------------------------------------------------

    public boolean checkSerialFromServer(devData checkDevData){
        try{
            JSONObject serialData = new JSONObject();
            serialData.put("serial_no", Integer.parseInt(checkDevData.getSerial_no()));
            String postJsonString = serialData.toString();
            String funcString = "/checkSerial";
            String urlString = "http://"+checkDevData.getIp()+":"+String.valueOf(checkDevData.getPort())+funcString;
            Log.i("chekcSerialFromServer", "urlString: "+urlString);
            String returnData = connectionWithURL(urlString, postJsonString);
            if(returnData.contains("ok")) return true;
// 테스트용함수           if(checkDevData.getSerial_no().equals("101") && checkDevData.getIp().equals("192.168.1.102")){
//                return true;
//            }
//            object = (JSONObject) new JSONTokener(returnData).nextValue();
        }catch (JSONException jsonException){
            jsonException.printStackTrace();
            Log.i("getAllTableFromServer", "Json파싱오류");
        }
        catch (IOException ioException){
            ioException.printStackTrace();
            Log.i("getAllTableFromServer", "커넥션 오류");
        }
        return false;
    }
    public String connectionWithURL(String urlString, String jsonString) throws IOException {
//        String urlString = "http://"+IPString+":"+PortString+funcString;
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
            conn.disconnect();
            return "";
        }

        try{

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
            conn.disconnect();
            Log.i("serverConnection","http 응답 코드 : " + responseCode+ "\nhttp 응답 데이터: "+returnData);
            return returnData;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
