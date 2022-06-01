package com.cookandroid.smartmirror;

import android.util.Log;

import com.cookandroid.smartmirror.dataClass.devData;
import com.cookandroid.smartmirror.dataClass.messageData;
import com.cookandroid.smartmirror.dataClass.userData;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
        messageData msgData = new messageData(3, 2, 0, "안녕하쇼", 2022, 5, 27, 12, 30, true);
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
    public void syncAllTableJson(){
        // 서버에서 syncAllTable를 통해 모든 Table 정보를 받아옵니다.
        String syncAllTable = "{"
                + " \"device\" : \"Pizza\", "
                + " \"locations\" : [ 94043, 90210 ],"
                + " \"tab1\" : [ 1, \"1\" ],"
                + " \"tab2\" : [ 2, \"2\" ],"
                + " \"tab3\" : [ 3, \"3\" ]"
                + "}";
        try{
            JSONObject object = (JSONObject) new JSONTokener(syncAllTable).nextValue();
            String query = object.getString("device");
            org.json.JSONArray locations = object.getJSONArray("locations");
            org.json.JSONArray tab3 = object.getJSONArray("tab3");

            Log.i("jsonParsing", "query: "+query);
            Log.i("jsonParsing", "locations: "+locations.toString());
            Log.i("jsonParsing", "tab3: "+tab3.toString());

        }catch (JSONException jsonException){
            jsonException.printStackTrace();
        }
    }
    public void httpMain(){
        System.out.println("[HttpURLConnection 사용해  post body json 방식 데이터 요청 및 응답 값 확인 실시]");

        /*[설 명]
         * 1. HttpURLConnection은 http 통신을 수행할 객체입니다
         * 2. URL 객체로 connection을 만듭니다
         * 3. 응답받은 결과를 InputStream으로 받아서 버퍼에 순차적으로 쌓습니다
         * */

        //데이터 정의 실시
        //포트 넘버뒤에 오는 URL을 통해 플라스크 서버에게 요청
        String url = "http://192.168.0.7:8000/getData";

        //하나하나 줄바꾸면서 쓰기 너무 어려워보임
        //String data2 = "{ \"userId\" : \"1\", \"id\" : \"1\" }"; //json 형식 데이터




        // JSONObject jsonObject = new JSONObject();

        // jsonObject.put("SECR_KEY", "ktko.tistory.com");
        // jsonObject.put("KEY", "ktko");

        //json 객체 생성 및 데이터 입력
        org.json.simple.JSONObject data1 = new org.json.simple.JSONObject();
        data1.put("BANK_CD", "088");
        data1.put("SEARCH_ACCT_NO", "1231231234");
        data1.put("ACNM_NO", "123456");
        data1.put("ICHE_AMT", "0");
        data1.put("TRSC_SEQ_NO", "0000001");

        org.json.simple.JSONObject data2 = new org.json.simple.JSONObject();
        data2.put("BANK_CD", "088");
        data2.put("SEARCH_ACCT_NO", "1231231234");
        data2.put("ACNM_NO", "123456");
        data2.put("ICHE_AMT", "0");
        data2.put("TRSC_SEQ_NO", "0000001");

        //json 배열 객체 생성
        JSONArray req_array = new JSONArray();
        //json 어레이에 두가지 데이터 추가
        req_array.add(data1);
        req_array.add(data2);

        // jsonObject.put("REQ_DATA", req_array);

        //json 형식 string 으로 변환
        String data3 = req_array.toString();

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
            try (OutputStream os = conn.getOutputStream()){
                byte request_data[] = ParamData.getBytes("utf-8");
                os.write(request_data);
                os.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            //http 요청 실시
            conn.connect();
            System.out.println("http 요청 방식 : "+"POST BODY JSON");
            System.out.println("http 요청 타입 : "+"application/json");
            System.out.println("http 요청 주소 : "+UrlData);
            System.out.println("http 요청 데이터 : "+ParamData);
            System.out.println("");

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
            System.out.println("http 응답 코드 : "+responseCode);
            System.out.println("http 응답 데이터 : "+returnData);

            //json 데이터 다루기, 파싱 작동 테스트
            JSONParser parser = new JSONParser();
            //json 서버에서 응답받은 데이터 어레이 객체로 파싱
            JSONArray arr = null;
            try {
                arr = (JSONArray)parser.parse(returnData);
            } catch (ParseException e) {
                System.out.println("변환에 실패");
                e.printStackTrace();
            }

            //파싱한 json 배열 크기만큼 반복, json 배열을 json 단일 객채로 파싱하고 데이터 사용
            System.out.println(arr.size());
            for(int i = 0; i<arr.size(); i++){
                System.out.println(arr.get(i));
                org.json.simple.JSONObject jObject = null;
                try {
                    jObject = (org.json.simple.JSONObject)parser.parse(arr.get(i).toString());
                } catch (ParseException e) {
                    System.out.println("변환에 실패");
                    e.printStackTrace();
                }
                //json 객체 작동 테스트
                System.out.println("i:"+ jObject.get("i"));
                System.out.println("j:"+ jObject.get("j"));

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //http 요청 및 응답 완료 후 BufferedReader를 닫아줍니다
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
