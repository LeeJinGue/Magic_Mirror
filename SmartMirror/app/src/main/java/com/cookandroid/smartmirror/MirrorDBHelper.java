package com.cookandroid.smartmirror;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cookandroid.smartmirror.dataClass.belongingSetData;
import com.cookandroid.smartmirror.dataClass.interestedStockData;
import com.cookandroid.smartmirror.dataClass.layoutData;
import com.cookandroid.smartmirror.dataClass.devData;
import com.cookandroid.smartmirror.dataClass.messageData;
import com.cookandroid.smartmirror.dataClass.scheduleData;
import com.cookandroid.smartmirror.dataClass.stockData;
import com.cookandroid.smartmirror.dataClass.userData;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class MirrorDBHelper extends SQLiteOpenHelper {
    private int dbVersion;
    private SQLiteDatabase db;
    private MirrorNetworkHelper networkHelper;

    // 더미데이터 - 레이아웃 ID
    private int set_layout_id = 1;
    public void setLayout_id(int user_num){
        set_layout_id = user_num * 100;
        Log.i("MirrorDBHelper", "레이아웃아이디 세팅: "+set_layout_id);
    }
    private int set_message_id = 1;
    public void setMessage_id(int user_num){set_message_id = user_num*1000;
        Log.i("MirrorDBHelper", "메세지 세팅: "+set_message_id);
    }
    private int set_belongingSet_id = 1;
    public void setBelongingSet_id(int user_num){set_belongingSet_id = user_num*1000;
        Log.i("MirrorDBHelper", "소지품세트 세팅: "+set_belongingSet_id);
    }

    public MirrorDBHelper(@Nullable Context context, int version) {
        super(context, "groupDB", null, 1);
        dbVersion = version;
        db = getWritableDatabase();
        networkHelper = new MirrorNetworkHelper();
        onCreate(db);
    }
    public void initDBbeforeLogin(FileInputStream fileInputStream){
        createDeviceTb(db);
        // json파일을 받아와서 한다고 해보자
        addDevData(networkHelper.getDeviceData());

        createStockListTb(db);
        if(!isExistStockList()){
            // 만약 stocklist정보가 없으면
            ExcelHelper excelHelper = new ExcelHelper(fileInputStream);
            addStockList(excelHelper.readStockExcelFile());
        }
    }

    public void initDB(){
        createUserTb(db);
        createLayoutSettingTb(db);
        createMessageTb(db);
        createScheduleTb(db);
        createStockTb(db);
        createBelongingsTb(db);

        networkHelper.getUserTable();

        // 테스트를 위해 userData를 미리 넣어둡니다.
        addUser(networkHelper.getUserData());
        addMessage(networkHelper.getMessageDate());
        messageData send = new messageData(1, 0, 2, "내가 보낸 메시지", 2022, 5, 20, 11, 30, true);
        messageData receive = new messageData(2, 2, 0, "상대가 보낸메세지", 2022, 5, 20, 10, 30, true);
        // 상대메시지 내매시지 잘나오나 확인
        addMessage(receive);
        addMessage(send);
        // 스케줄 데이터를 넣어봅니다.
        scheduleData newData = new scheduleData(11, 3, "2022-05-28 13:10:00", "2022-05-28 14:10:00", "휴");
        addSchedule(newData);
        scheduleData newData2 = new scheduleData(21, 3, "2022-05-27 13:10:00", "2022-05-27 14:10:00", "휴");
        addSchedule(newData2);
        // 소지품리스트 데이터를 넣어봅니다.
        addBelongingSet(new belongingSetData(2, 3,"화요일","0", "Test Java", "신분증,물통,커피"));

        // 관심주식 데이터를 넣어봅니다.
        addInterestedStock(new interestedStockData(11, 0, "삼성전자", "1"));
        Log.i("DataBase", "DB초기화");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS groupTBL ( gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");

    }
    public void getDevData(){
        createDeviceTb(db);
        // dev데이터를 미리 넣어둡니다.
        addDevData(networkHelper.getDevData());


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS groupTBL");
    }
    public void createDeviceTb(SQLiteDatabase db){
        // device 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS device");
        db.execSQL("CREATE TABLE IF NOT EXISTS device " +
                "(serial_no VARCHAR(50) PRIMARY KEY, " +
                "ip VARCHAR(20) DEFAULT NULL, " +
                "port INTEGER(11) DEFAULT NULL, " +
                "location VARCHAR(255) DEFAULT NULL, " +
                "info VARCHAR(255) DEFAULT NULL);");
        Log.i("Create Table", "device table 생성 완료");
        // Device정보 초기값(IP주소, 시리얼넘버) 넣어놓기

    }
    public void createUserTb(SQLiteDatabase db){
        // user 테이블 생성
        // 0: 유저아이디, 1: 시리얼넘버, 2: 이름, 3: 유저 이미지 경로
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("CREATE TABLE IF NOT EXISTS user " +
                "(user_num INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "serial_no INTEGER(11), " +
                "name VARCHAR(100) NOT NULL, " +
                "user_image_pass VARCHAR(255) NOT NULL" +
//                ", FOREIGN KEY(serial_no) " +
//                "REFERENCES device(serial_no)" +
                        ");");
        Log.i("Create Table", "user table 생성 완료");
    }
    public void createLayoutSettingTb(SQLiteDatabase db){
        // layoutsetting 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS layoutsetting");
        db.execSQL("CREATE TABLE IF NOT EXISTS layoutsetting " +
                "(layout_id INTEGER(11) PRIMARY KEY, " +
                "user_num INTEGER(11) NOT NULL, " +
                "type INTEGER(11) NOT NULL, " +
                "loc INTEGER(11) NOT NULL" +
//                ", FOREIGN KEY(user_num)" +
//                "REFERENCES user(user_num)" +
                ");");
        Log.i("Create Table", "layout setting table 생성 완료");
    }
    public void createBelongingsTb(SQLiteDatabase db){
        // belongings 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS belongings");
        db.execSQL("CREATE TABLE IF NOT EXISTS belongings " +
                "(belonging_id INT(11) PRIMARY KEY, " +
                "user_num INTEGER(11), " +
                "set_name VARCHAR(255) NOT NULL, " +
                "activation VARCHAR(10), " +
                "set_info VARCHAR(255), " +
                "stuff_list TEXT" +
//                ", FOREIGN KEY(user_num)" +
//                "REFERENCES user(user_num)" +
                ");");
        Log.i("Create Table", "belongings table 생성 완료");
    }
    public void createMessageTb(SQLiteDatabase db){
        // message 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS message");
        db.execSQL("CREATE TABLE IF NOT EXISTS message " +
                "(message_id INTEGER(11) PRIMARY KEY, " +
                "user_num INTEGER(11), " +
                "sender_num INTEGER(11), " +
                "text VARCHAR(100) DEFAULT NULL, " +
                "date DATETIME DEFAULT (datetime('now', 'localtime'))"+
//                ", FOREIGN KEY(receiver_num)" +
//                "REFERENCES user(user_num)," +
//                "FOREIGN KEY(sender_num)" +
//                "REFERENCES user(user_num)" +
                ");");
        Log.i("Create Table", "message table 생성 완료");
    }
    public void createScheduleTb(SQLiteDatabase db){
        // schedule 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS schedule");
        db.execSQL("CREATE TABLE IF NOT EXISTS schedule " +
                "(schedule_id INTEGER(11) PRIMARY KEY, " +
                "user_num INTEGER(11), " +
                "start_time DATETIME DEFAULT NULL, " +
                "end_time DATETIME DEFAULT NULL, " +
//                "date DATETIME DEFAULT NULL, " +
                "text VARCHAR(255) DEFAULT NULL" +
//                ", FOREIGN KEY(user_num)" +
//                "REFERENCES user(user_num)" +
                ");");
        Log.i("Create Table", "schedule table 생성 완료");
    }
    public void createStockTb(SQLiteDatabase db){
        // stock 테이블 생성
        db.execSQL("DROP TABLE IF EXISTS stock");
        db.execSQL("CREATE TABLE IF NOT EXISTS stock " +
                "(stock_id INTEGER(11) PRIMARY KEY, " +
                "user_num INTEGER(11), " +
                "stock_name VARCHAR(255) NOT NULL, " +
                "stock_code VARCHAR(50) NOT NULL" +
//                ", FOREIGN KEY(user_num)" +
//                "REFERENCES user(user_num)" +
                ");");
        Log.i("Create Table", "stock table 생성 완료");
    }

    // ----------------------stockList 관련 --------------------------
    public void addStockList(ArrayList<stockData> stockList){
//        public void addStockList(FileInputStream stockListFile){
//        ExcelHelper excelHelper = new ExcelHelper(stockListFile);
//        ArrayList<stockData> stockList = excelHelper.readStockExcelFile();
        // stock테이블에 xls파일 읽어서 넣어줌.
        for(stockData nowStock:stockList){
            ContentValues stockRow = new ContentValues();
            stockRow.put("stock_code", nowStock.getStock_code());
            stockRow.put("stock_name",nowStock.getStock_name());
            long result =db.insert("stocklist", null, stockRow);
            if(result != -1){
//                Log.i("addStockList", "stock_code: "+nowStock.getStock_code() +", stock_name: "+ nowStock.getStock_name());
            }else{
                Log.i("addStockList", "db insert 오류, stockData: "+nowStock.toString());
            }
        }
        Log.i("addStockList", "Excel값을 읽어와서 stocklist table에 추가 완료");
    }
    public boolean isExistStockList(){
        // stocklist의 개수를 가져옵니다.
        Cursor stockCursor = db.rawQuery("SELECT COUNT(*) FROM stocklist;", null);
        stockCursor.moveToFirst();
        int count = stockCursor.getInt(0);
        Log.i("stockCursorTest","count: "+count);
        if(count == 0){
            // count가 0이면 없는거니까 false 리턴
            return false;
        }else{
            // count가 1이상이면 하나라도 있으니까 true 리턴
            return true;
        }
    }
    // ----------------------------------------------------------------

    public void createStockListTb(SQLiteDatabase db) {
        // stocklist 테이블 생성
//        db.execSQL("DROP TABLE IF EXISTS stocklist");
        db.execSQL("CREATE TABLE IF NOT EXISTS stocklist " +
                "(stock_code VARCHAR(50) PRIMARY KEY, " +
                "stock_name VARCHAR(255) NOT NULL " +
//                ", FOREIGN KEY(user_num)" +
//                "REFERENCES user(user_num)" +
                ");");

        Log.i("Create Table", "stocklist table 생성 완료");
    }

    // -----------------------User관련--------------------------------
    // userData List로 갖고오기
    public ArrayList<userData> getAllUserList(){
        Cursor mCursor = db.rawQuery("SELECT * FROM user", null);
        ArrayList<userData> list = new ArrayList<userData>();
        if(mCursor.moveToFirst()){
            do {
                int id = mCursor.getInt(0);
                String serial_no = mCursor.getString(1);
                String name = mCursor.getString(2);
                String usr_img_path = mCursor.getString(3);
//                Log.i("getUserData", "id: "+id+", serial_no: "+serial_no+", name: "+name+", user_image_path: "+usr_img_path+" 유저데이터를 가져옵니다.");
                list.add(new userData(id, serial_no, name, usr_img_path));
            }while(mCursor.moveToNext());
        }
        for(userData u:list) Log.i("getUserData", "id: "+u.getUser_num()+", serial_no: "+u.getSerial_no()+", name: "+u.getName()+", user_image_path: "+u.getUser_image_pass()+" 유저데이터를 가져옵니다.");
        mCursor.close();
        if(list.isEmpty()){
            return null;
        }
        return list;
    }

    // user추가
    public void addUser(userData newUser){
        db.execSQL("INSERT INTO user VALUES(" +
                newUser.getUser_num() +
                ", "+ newUser.getSerial_no()+
                ",'"+ newUser.getName()+
                "','"+ newUser.getUser_image_pass()+
                "');");
        Log.i("addUserData", "신규유저 "+newUser.toString()+" 추가");
    }

    // user 수정
    public void editUserName(userData editUser){

        db.execSQL("UPDATE user SET name = '"+ editUser.getName() + "'"
        + "WHERE user_num = "+editUser.getUser_num());
        Log.i("editUserName", " "+editUser.toString()+"로 수정");

    }

    // user 삭제
    public void delUser(userData delUser){
        db.delete("user", "user_num=?",new String[]{String.valueOf(delUser.getUser_num())});
//        db.execSQL("DELETE FROM user WHERE user_num ="+delUser.getUser_num());
        Log.i("delUser", " "+delUser.toString()+"삭제");
    }

    // ---------------------------------------------------------------

    // --------------------------Device 관련-------------------------
    // IP주소, 시리얼넘버가 맞는지 체크
    public boolean checkIPAddressAndSerial(String IPAddress, String Serial, int port){
        // DB에 있는 Data 받아올 변수들
        String db_serial = "";
        int db_port = -1;
        String db_ip = "";

        // 시리얼번호, IP, Port데이터 갖고옵니다.
        Cursor cs = db.rawQuery("SELECT serial_no, ip, port FROM device", null);

        // Cursor객체를 통해 DB에 저장된 데이터를 읽어옵니다.
        while(cs.moveToNext()){
            db_serial = cs.getString(0);
            db_ip = cs.getString(1);
            db_port = cs.getInt(2);
            Log.i("checkIPAddressAndSerial", "\ndb_serial: "+db_serial+"\ndb_ip: "+db_ip+"\ndb_port: "+db_port);
            Log.i("checkIPAddressAndSerial", "\nserial: "+Serial+"\nip: "+IPAddress+"\nport: "+port);

        }
        // 받아온 ip가 ""라면 -> DB에 저장된게 없다면 추가해야하는데?
        if(db_ip.equals("")){
            Log.i("DBHelper", "checkIPAddressAndSerial - 저장된 IP / Serial 정보가 없습니다.");
            return false;
        }
        if(db_ip.equals(IPAddress)){
            if(db_serial.equals(Serial)){
                if(db_port == port){
                    // IP주소가 같고 시리얼넘버도 같다면
                    return true;

                }else{
                    Log.i("DBHelper", "checkIPAddressAndSerial - Port 정보가 일치하지 않습니다.");
                    return false;
                }
            }else{
                // IP주소는 같지만 serial넘버가 다르다.
                Log.i("DBHelper", "checkIPAddressAndSerial - Serial 정보가 일치하지 않습니다.");
                return false;
            }
        }
        // IP주소가 다르다.
        Log.i("DBHelper", "checkIPAddressAndSerial - IP주소가 일치하지 않습니다.");
        cs.close();
        return false;
    }

    // 새로운 IP주소, 시리얼넘버 정보를 추가
    public void addDevData(devData devData){
        // 시리얼넘버, IP주소, 포트넘버, location, info순
        db.execSQL("INSERT INTO device VALUES(" +
                devData.getSerial_no() +
                ", '" + devData.getIp() +
                "', " + devData.getPort() +
                ", '" + devData.getLocation() +
                "', '" + devData.getInfo()+
                "');");
        Log.i("addDevData", "새 데이터 "+devData.toString()+" 추가");

    }
    // ---------------------------------------------------------------

    // --------------------------Layout 관련---------------------------
    // 적용버튼 눌렀을 때 작동하는, 세팅을 DB에 저장하는 함수
    public void layoutSet(ArrayList<layoutData> layoutDataList, userData selectedUser){

        // layoutsetting 테이블에서 받은 userData에 있는 user_num와 같은 user_num인 Row들 삭제
        db.delete("layoutsetting", "user_num=?", new String[]{String.valueOf(selectedUser.getUser_num())});
        // 새 layoutData들 모아서 추가
        for(layoutData layoutData:layoutDataList){
            ContentValues values = new ContentValues();
            values.put("layout_id", ++set_layout_id);
            values.put("user_num", selectedUser.getUser_num());
            values.put("type", layoutData.getType());
            values.put("loc", layoutData.getLoc());
            long result =db.insert("layoutsetting", null, values);

            if(result != -1){
                Log.i("layoutSet", layoutData.toString() + ", id: "+ set_layout_id+" 레이아웃 추가");

            }else{
                Log.i("layoutSet", "db insert 오류");
            }
        }
    }

    public ArrayList<layoutData> getLayoutDataListByUser(userData selectedUser){
        ArrayList<layoutData> layoutDataArrayList = new ArrayList<>();
        Cursor layoutCursor = db.rawQuery("SELECT * FROM layoutsetting WHERE user_num="+selectedUser.getUser_num()+";", null);
        while(layoutCursor.moveToNext()){
            int layout_id = layoutCursor.getInt(0);
            int user_num = layoutCursor.getInt(1);
            int type = layoutCursor.getInt(2);
            int loc = layoutCursor.getInt(3);
            layoutData newLayoutData = new layoutData(layout_id, user_num, loc, type);
            layoutDataArrayList.add(newLayoutData);
            Log.i("getLayoutDataListByUser", newLayoutData.toString() + " 레이아웃 데이터 갖고옴");
        }
        layoutCursor.close();
        return layoutDataArrayList;
    }
    public ArrayList<layoutData> getAllLayoutData(){
        ArrayList<layoutData> layoutDataArrayList = new ArrayList<>();
        Cursor layoutCursor = db.rawQuery("SELECT * FROM layoutsetting;", null);
        while(layoutCursor.moveToNext()){
            int layout_id = layoutCursor.getInt(0);
            int user_num = layoutCursor.getInt(1);
            int type = layoutCursor.getInt(2);
            int loc = layoutCursor.getInt(3);
            layoutData newLayoutData = new layoutData(layout_id, user_num, loc, type);
            layoutDataArrayList.add(newLayoutData);
            Log.i("getAllLayoutDataList", newLayoutData.toString() + ", id: "+newLayoutData.getLayout_id()+ " 레이아웃 데이터 갖고옴");
        }
        layoutCursor.close();
        return layoutDataArrayList;
    }
    // ----------------------------------------------------------------

    // --------------------------Message 관련---------------------------

    public void addMessage(messageData newMessage){
        // 메세지아이디,
        Log.i("addMessage", "메시지아이디 체크: "+newMessage.getMessage_id());
        db.execSQL("INSERT INTO message VALUES(" +
                newMessage.getMessage_id()+
                ", " + newMessage.getUser_num() +
                ", " + newMessage.getSender_num() +
                ", '" + newMessage.getText() +
                "', '" + newMessage.getYear()+"년 "+ newMessage.getMonth()+"월 "+ newMessage.getDate()+"일 "+
                newMessage.getHour()+"시 "+newMessage.getMinute()+ "분"+
                "');");
        newMessage.setMessage_id(set_message_id);
        Log.i("addMessage", "새 메세지 "+newMessage.toString()+" 추가");
    }
    public void delMessage(messageData delMessage){
        // 메세지아이디,
        Log.i("delMessage", "메시지아이디 체크: "+delMessage.getMessage_id());
        db.delete("message", "message_id=?", new String[]{String.valueOf(delMessage.getMessage_id())});
    }

    // isRecieved==true -> 너가 보내고 내가 받은 메시지 데이터를 받아옴
    // isRecieved==false -> 내가 보내고 너가 받은 메시지 데이터를 받아옴
    public ArrayList<messageData> getMeesageList(userData me, userData you, boolean isReceived){
        ArrayList<messageData> messageList = new ArrayList<>();
        Cursor msgCursor;
        if(isReceived){
            // 내가 받고 상대가 보낸 메시지 리스트. isleft = true
            Log.i("MirrorDBHelper", "내가 받고 상대가 보낸 메시지 리스트를 가져옵니다. 받는사람: "+me.getName()+", 보낸사람: "+you.getName());
            msgCursor = db.rawQuery("SELECT * FROM message WHERE user_num="+me.getUser_num()+" AND sender_num="+ you.getUser_num()+";", null);

        }else{
            // 내가 보내서 상대가 받은 메시지 리스트. isleft = false;
            Log.i("MirrorDBHelper", "내가 보내고 상대가 받은 메시지 리스트를 가져옵니다. 받는사람: "+you.getName()+", 보낸사람: "+me.getName());
            msgCursor = db.rawQuery("SELECT * FROM message WHERE user_num="+you.getUser_num()+" AND sender_num="+ me.getUser_num()+";", null);

        }
        while (msgCursor.moveToNext()){
            int message_id = msgCursor.getInt(0);
            int receiver_num = msgCursor.getInt(1);
            int sender_num = msgCursor.getInt(2);
            String text = msgCursor.getString(3);
            String dateTime = msgCursor.getString(4);
            String[] year = dateTime.split("년 ");
            String[] month = year[1].split("월 ");
            String[] date = month[1].split("일 ");
            String[] hour = date[1].split("시 ");
            String[] minute = hour[1].split("분");
            messageData newMessage = new messageData(message_id, receiver_num, sender_num, text,
                    Integer.parseInt(year[0]), Integer.parseInt(month[0]),Integer.parseInt(date[0]),
                    Integer.parseInt(hour[0]), Integer.parseInt(minute[0]), isReceived);
            Log.i("getMessageList","메세지: "+newMessage.toString());
            messageList.add(newMessage);
        }
        msgCursor.close();
        return messageList;
    }

    // ----------------------------------------------------------------


    // --------------------------Schedule 관련---------------------------

    public void addSchedule(scheduleData newSchedule){
        // 메세지아이디,
        Log.i("addSchedule", "스케줄아이디 체크: "+newSchedule.getSchedule_id());
        db.execSQL("INSERT INTO schedule VALUES(" +
                newSchedule.getSchedule_id()+
                ", " + newSchedule.getUser_num() +
                ", '" + newSchedule.getStartTime() +
                "', '" + newSchedule.getEndTime() +
                "', '" + newSchedule.getTitle() +
//                "', '" + newSchedule.getDate() +
                "');");
//        newSchedule.setMessage_id(set_message_id);
        Log.i("addSchedule", "새 일정 "+newSchedule.toString()+" 추가");
    }
    public void editSchedule(scheduleData editSchedule){
        Log.i("editSchedule", "수정된 일정: "+editSchedule.toString());
        db.execSQL("UPDATE schedule " +
                "SET start_time = '"+editSchedule.getStartTime() +
                "', end_time = '" + editSchedule.getEndTime()+
                "', text = '" + editSchedule.getTitle() +
                "' WHERE schedule_id = "+editSchedule.getSchedule_id()+
                ";");
    }
    public void delSchedule(scheduleData delSchedule){
        Log.i("delSchedule", "삭제할 일정: "+delSchedule.toString());
        db.delete("schedule", "schedule_id=?", new String[]{String.valueOf(delSchedule.getSchedule_id())});
    }
    public ArrayList<scheduleData> getScheduleByDate(String selectedDate, userData selectedUser){
        ArrayList<scheduleData> scheduleDataList = new ArrayList<>();
        // schedule table COlumn에서 date를 날짜, 시간으로 나눠야 할 것 같다.
        // schedule_id, user_num, start_time, end_time, text, date?;

        Cursor scheduleCursor = db.rawQuery("SELECT * " +
                "FROM schedule " +
                "WHERE" +
                " user_num="+ selectedUser.getUser_num() +
                " AND start_time >= '"+selectedDate+
                "' AND start_time <= '" + Methods.getNextDate(selectedDate) +
                "';", null);
        while (scheduleCursor.moveToNext()){
            int schedule_id = scheduleCursor.getInt(0);
            int user_num = scheduleCursor.getInt(1);
            String start_time = scheduleCursor.getString(2);
            String end_time = scheduleCursor.getString(3);

            String text = scheduleCursor.getString(4);
//            String dateTime = scheduleCursor.getString(5);
//            String[] year = dateTime.split("년 ");
//            String[] month = year[1].split("월 ");
//            String[] date = month[1].split("일 ");
//            String[] hour = date[1].split("시 ");
//            String[] minute = hour[1].split("분");
            scheduleData newSchedule = new scheduleData(schedule_id, user_num, start_time,end_time, text);
            Log.i("getScheduleByDate","일정: "+newSchedule.toString());
            scheduleDataList.add(newSchedule);
        }
        scheduleCursor.close();
        return scheduleDataList;
    }
    // ----------------------------------------------------------------

    // ----------------------------Belonging관련-----------------------------

    public ArrayList<belongingSetData> getBelongingSetDataByUser(userData selectedUser){
        ArrayList<belongingSetData> belongingSetDataList = new ArrayList<>();
        // activation == 0 비활성화, 1 활성화
        // belonging_id, user_num, set_name, activation, set_info, stuff_list

        Cursor belongingCursor = db.rawQuery("SELECT * " +
                "FROM belongings " +
                "WHERE" +
                " user_num="+ selectedUser.getUser_num() +
                ";", null);
        while (belongingCursor.moveToNext()){
            int belonging_id = belongingCursor.getInt(0);
            int user_num=belongingCursor.getInt(1);
            String set_name=belongingCursor.getString(2);
            String activation=belongingCursor.getString(3);
            String set_info=belongingCursor.getString(4);
            String stuff_list=belongingCursor.getString(5);
            belongingSetData newBelongingSet = new belongingSetData(belonging_id, user_num, set_name, activation, set_info, stuff_list);
            Log.i("getBelongingSetDataByUser","일정: "+newBelongingSet.toString());
            belongingSetDataList.add(newBelongingSet);
        }
        belongingCursor.close();
        return belongingSetDataList;
    }

    public void addBelongingSet(belongingSetData newBelongingSet){
        Log.i("addBelongingSetWithUser", "DB에 추가할 belongingSet 이름: "+newBelongingSet.getSet_name()+", 유저ID: "+newBelongingSet.getUser_num());
        ContentValues values = new ContentValues();
        values.put("belonging_id", newBelongingSet.getBelonging_id());
        values.put("set_name", newBelongingSet.getSet_name());
        values.put("activation", newBelongingSet.getActivation());
        values.put("set_info", newBelongingSet.getSet_info());
        values.put("user_num", newBelongingSet.getUser_num());
        values.put("stuff_list", newBelongingSet.getStuff_list_str());
        long result =db.insert("belongings", null, values);
        if(result != -1){
            Log.i("addBelongingSetWithUser", newBelongingSet.toString() + ", id: "+ newBelongingSet.getBelonging_id()+" 소지품세트 추가");
//            newBelongingSet.setBelonging_id(set_belongingSet_id);
        }else{
            Log.i("addBelongingSetWithUser", "db insert 오류");
        }
    }
    public void delBelongingSet(belongingSetData delBelongingSet){

        Log.i("delBelongingSet", "삭제할 소지품 세트: "+delBelongingSet.toString());
        db.delete("belongings", "belonging_id=?", new String[]{String.valueOf(delBelongingSet.getBelonging_id())});

    }
    public void editBelongingSet(belongingSetData editBelongingSet){
        Log.i("editBelongingSet", "수정할 소지품 세트: "+editBelongingSet.toString());
        db.execSQL("UPDATE belongings " +
                "SET set_name = '"+editBelongingSet.getSet_name() +
                "', set_info = '" + editBelongingSet.getSet_info()+
                "', stuff_list = '" + editBelongingSet.getStuff_list_str() +
                "', activation = '" + editBelongingSet.getActivation() +
                "' WHERE belonging_id = "+editBelongingSet.getBelonging_id()+
                ";");
        Log.i("editBelongingSet", "수정된 소지품 세트: "+editBelongingSet.toString());
    }
    public void setBelongingSetActiavted(belongingSetData activatedBelongingSet){
        Log.i("setBelongingSetActivated", "수정할 소지품 세트 활성화: "+activatedBelongingSet.getActivation());
        db.execSQL("UPDATE belongings " +
                "SET activation = '" + activatedBelongingSet.getActivation() +
                "' WHERE belonging_id = "+activatedBelongingSet.getBelonging_id()+
                ";");
        Log.i("setBelongingSetActivated", "수정된 소지품 세트 활성화: "+activatedBelongingSet.getActivation());

    }


    // ----------------------------------------------------------------
    // --------------------------------관심주식목록----------------------
    public ArrayList<interestedStockData> getInterestedStockByUser(userData selectedUser){
        ArrayList<interestedStockData> interestedStockList = new ArrayList<>();
        Cursor stockCursor = db.rawQuery("SELECT * " +
                "FROM stock " +
                "WHERE" +
                " user_num="+ selectedUser.getUser_num() +
                ";", null);
        while (stockCursor.moveToNext()){
            int stock_id = stockCursor.getInt(0);
            int user_num = stockCursor.getInt(1);
            String stock_name=stockCursor.getString(2);
            String stock_code=stockCursor.getString(3);
            interestedStockData newInterstedStock = new interestedStockData(stock_id, user_num, stock_name, stock_code);
            Log.i("getStockDataByUser","관심주식: "+newInterstedStock.toString());
            interestedStockList.add(newInterstedStock);
        }
        stockCursor.close();
        return interestedStockList;
    }

    public void addInterestedStock(interestedStockData newInterestedStock){
        Log.i("addInterestedStock", "DB에 추가할 stock 이름: "+newInterestedStock.getStock_name()+", 유저ID: "+newInterestedStock.getUser_num());
        ContentValues values = new ContentValues();
        values.put("stock_id", newInterestedStock.getStock_id());
        values.put("user_num", newInterestedStock.getUser_num());
        values.put("stock_name", newInterestedStock.getStock_name());
        values.put("stock_code", newInterestedStock.getStock_code());
        long result =db.insert("stock", null, values);
        if(result != -1){
            Log.i("addInterestedStock", newInterestedStock.toString() + ", id: "+ newInterestedStock.getStock_id()+" 관심주 추가");
//            newBelongingSet.setBelonging_id(set_belongingSet_id);
        }else{
            Log.i("addInterestedStock", "db insert 오류");
        }
    }
    public void delInterestedStock(interestedStockData delInterestedStock){
        Log.i("delInterestedStock", "삭제할 관심주: "+delInterestedStock.toString());
        db.delete("stock", "stock_id=?", new String[]{String.valueOf(delInterestedStock.getStock_id())});
    }
    // ----------------------------------------------------------------
}

