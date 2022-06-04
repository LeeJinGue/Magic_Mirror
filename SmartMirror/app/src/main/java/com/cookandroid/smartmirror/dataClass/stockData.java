package com.cookandroid.smartmirror.dataClass;

public class stockData {
    private String stock_code;
    private String stock_name;
    public stockData(String stock_code, String stock_name){
        this.stock_code=stock_code;
        this.stock_name=stock_name;
    }
    public String toString(){
        return "stock_code: "+stock_code+", stock_name: "+stock_name;
    }
    public String getStock_code() { return stock_code; }
    public String getStock_name() { return stock_name; }
}
