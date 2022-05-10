package com.cookandroid.smartmirror.activities;

import static com.cookandroid.smartmirror.Methods.ConvertDPtoPX;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.adapter.BelongingItemRecyclerAdapter;

import java.util.ArrayList;

public class BelongingSetAddActivity extends AppCompatActivity {
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
    Context context;
    EditText belongingNameEditText;
    Button belongingSetAddBtn;
    RecyclerView belongingItemRecyclerView;
    BelongingItemRecyclerAdapter belongingItemRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_belonging_set_add);
        context = getApplicationContext();
        // Add Coustom AppBar & Set Title Color Gradient
        Toolbar toolbar = (Toolbar) findViewById(R.id.belongingSetAddAppBar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbarTv);
        tvTitle.setText("소지품세트 추가");
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);

        // 소지품 세트의 이름을 입력하는 EditText
        belongingNameEditText = findViewById(R.id.belongingSetNameEditText);
        // 소지품 세트를 추가하는(현재까지 입력한 소지품들을 저장하는) 버튼
        belongingSetAddBtn = findViewById(R.id.belongingSetAddBtn);
        // 현재 소지품 세트의 소지품 아이템 리스트를 보여주는 Recycler View
        belongingItemRecyclerView = findViewById(R.id.belongingItemRecyclerView);
        ArrayList<String> belongingItemList = new ArrayList<>();
        belongingItemRecyclerAdapter = new BelongingItemRecyclerAdapter(belongingItemList);
        belongingItemRecyclerView.setAdapter(belongingItemRecyclerAdapter);
        belongingItemRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        belongingItemRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        belongingItemList.add("카메라");
        belongingItemList.add("핸드폰");
        belongingItemList.add("지갑");
        belongingItemRecyclerAdapter.addItem("소지품명1");
        belongingItemRecyclerAdapter.notifyDataSetChanged();


    }
}