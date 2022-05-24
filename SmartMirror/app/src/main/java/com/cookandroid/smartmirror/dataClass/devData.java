package com.cookandroid.smartmirror.dataClass;

// 디바이스 정보 객체
public class devData {
    private int serial_no;
    private String ip;
    private int port;
    private String location;
    private String info;
    public String toString(){
        return  "dev 정보:\nserial_no: "+serial_no+", ip: "+ip+", port: "+port+", location: "+location+", info: "+info;
    }
    public devData(int serial_no, String ip, int port, String location, String info){
        this.serial_no = serial_no;
        this.ip = ip;
        this.port = port;
        this.location = location;
        this.info = info;
    }

    public int getSerial_no() {
        return serial_no;
    }

    public int getPort() {
        return port;
    }

    public String getInfo() {
        return info;
    }

    public String getIp() {
        return ip;
    }

    public String getLocation() {
        return location;
    }
}
