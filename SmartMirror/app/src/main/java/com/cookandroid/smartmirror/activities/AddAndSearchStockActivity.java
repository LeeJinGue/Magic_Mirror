package com.cookandroid.smartmirror.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.MirrorDBHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.adapter.StockSearchRecyclerAdapter;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.interestedStockData;

import java.util.ArrayList;

public class AddAndSearchStockActivity extends AppCompatActivity implements StockSearchRecyclerAdapter.onItemListener{
    Button stockAddEndBtn;
    String selectedStockName;
    interestedStockData addedStock;
    StockSearchRecyclerAdapter mAdapter;
    ArrayList<String> stockNameList;
    RecyclerView stockSearchResultRecyclerView;
    SearchView searchView;
    MyApplication myApp;
    MirrorDBHelper sqlDB;
    public ArrayList<String> getStockNameList(){
        ArrayList<String> stockNameListFromDB = sqlDB.getAllStockNameList();
        return stockNameListFromDB;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_search_stock);

        // Add Coustom AppBar & Set Title Color Gradient
        Toolbar toolbar = (Toolbar) findViewById(R.id.stockAddAppBar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbarTv);
        tvTitle.setText("관심주 추가");
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);
        
        // 전역변수를 관리할 객체
        myApp = (MyApplication) getApplicationContext();
        sqlDB = new MirrorDBHelper(getApplicationContext(), 1);
        stockAddEndBtn = findViewById(R.id.stockAddEndBtn);
        stockSearchResultRecyclerView = findViewById(R.id.stockSearchResultRecyclerView);
        stockAddEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStockName = searchView.getQuery().toString();
                if(stockNameList.contains(selectedStockName)){
                    interestedStockData addedStock = new interestedStockData(++myApp.stockId,myApp.getSelectedUser().getUser_num(), selectedStockName,"1");
                    Intent intent = new Intent();
                    intent.putExtra("addStock", addedStock);
                    setResult(RESULT_OK, intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), "주식 이름이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stockNameList = getStockNameList();
        mAdapter = new StockSearchRecyclerAdapter(stockNameList);
        mAdapter.setOnItemListener(this);
        stockSearchResultRecyclerView.setAdapter(mAdapter);
        stockSearchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        stockSearchResultRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = findViewById(R.id.stockSearchView);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                mAdapter.filter(text);
                return false;
            }
        });
    }


    @Override
    public void onItemClicked(String name) {
        Log.i("AddAndSearchStockActivity", "전달받은 주식이름: "+name);
        searchView.setQuery(name, true);
    }
}