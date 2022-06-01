package com.cookandroid.smartmirror.activities;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cookandroid.smartmirror.Methods;
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
    public void getStockNameList(){
        stockNameList = new ArrayList<>();
        stockNameList.add("주식1");
        stockNameList.add("주식1");
        stockNameList.add("주식2");
        stockNameList.add("주식3");
        stockNameList.add("Samsung");
        stockNameList.add("LG전자");
        stockNameList.add("Apple");
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
        
        
        myApp = (MyApplication) getApplicationContext();
        getStockNameList();
        stockAddEndBtn = findViewById(R.id.stockAddEndBtn);
        stockSearchResultRecyclerView = findViewById(R.id.stockSearchResultRecyclerView);
        stockAddEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStockName = searchView.getQuery().toString();
                interestedStockData addedStock = new interestedStockData(++myApp.stockId,myApp.getSelectedUser().getUser_num(), selectedStockName,"1");
                Intent intent = new Intent();
                intent.putExtra("addStock", addedStock);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

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

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
//            // 검색어는 SearchManager.QUERY라는 string extra로 보내집니다.
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            search(query);
//        }
//    }
//    void search(String query){
//        Log.i("AddAndSearchStockActivity", "Search중");
//        ArrayList<String> stockNames = new ArrayList<>();
//        stockNames.add("주식1");
//        stockNames.add("주식2");
//        stockNames.add("주식3");
//        stockNames.add("삼성전자");
//        stockNames.add("LG전자");
//        stockNames.add("테슬라");
//        for(String stockName: stockNames){
//            if(stockName.toLowerCase().contains(query.toLowerCase())){
//                selectedStockName = stockName;
//                return;
//            }
//        }
//
//
//
//    }

    @Override
    public void onItemClicked(String name) {
        Log.i("AddAndSearchStockActivity", "전달받은 주식이름: "+name);
        searchView.setQuery(name, true);
    }
}