package com.cookandroid.smartmirror.helper;

import android.util.Log;

import com.cookandroid.smartmirror.dataClass.stockData;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class ExcelHelper {
    FileDescriptor fileDescriptor;
    private FileInputStream fileInputStream;
    public ExcelHelper(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }
    public ArrayList<stockData> readStockExcelFile(){
        try{
            ArrayList<stockData> stockDataList = new ArrayList<>();
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fileInputStream);
            // Get the first sheet from workbook
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno =0;
            String test = "\n";
    //            textView.append("\n");
            while (rowIter.hasNext()) {
//                Log.i("readExcelFileFromAssets", " row no "+ rowno );
                XSSFRow myRow = (XSSFRow) rowIter.next();
                if(rowno !=0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno =0;
                    String stockName="", stockCode="";
                    while (colno<=1) {
                        XSSFCell myCell = (XSSFCell) cellIter.next();
                        if (colno==0){
                            stockName = myCell.toString();
                        }else if (colno==1){
                            stockCode = myCell.toString();
                        }
                        colno++;
//                        Log.i("readExcelFileFromAssets", " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                    }
                    stockData newStock = new stockData(stockCode, stockName);
                    stockDataList.add(newStock);
                    test+=( stockName + " -- "+ stockCode+ "  -- "+"\n");
                }
                rowno++;
            }
            Log.i("readExcelFileFromAssets", "주식개수: "+stockDataList.size());
            return stockDataList;
        } catch (Exception e) {
//            Log.e("readExcelFileFromAssets", "error "+ e.toString());
        }
        return null;
    }
}
