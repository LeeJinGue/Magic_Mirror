package com.cookandroid.smartmirror.activities;

import static com.cookandroid.smartmirror.Methods.ConvertDPtoPX;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.adapter.BelongingItemRecyclerAdapter;
import com.cookandroid.smartmirror.dataClass.belongingSetData;

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
    EditText belongingNameEditText, belongingDetailEditText;
    Button belongingSetAddBtn;
    ImageView belongingItemAddBtn;
    RecyclerView belongingItemRecyclerView;
    BelongingItemRecyclerAdapter belongingItemRecyclerAdapter;
    boolean isChecked;
    // 수정할 / 추가할 소지품 세트의 인덱스
    int index;
    ArrayList<String> belongingItemList;
//    belongingSetData addedBelongingSet;
    public void getSetItemData(){
        belongingItemList.add("카메라");
        belongingItemList.add("핸드폰");
        belongingItemList.add("지갑");
        belongingItemList.add("소지품명1");
    }
    // 소지품 추가버튼 클릭시 생성되는 Dialog에 소지품 이름을 입력하는 EditText
    private EditText nameEditText;

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
        // 소지품 세트의 상세설명을 입력하는 EditText
        belongingDetailEditText = findViewById(R.id.belongingSetDetailEditText);
        // 소지품 세트를 추가하는(현재까지 입력한 소지품들을 저장하는) 버튼
        belongingSetAddBtn = findViewById(R.id.belongingSetAddBtn);
        // 현재 소지품 세트의 소지품 아이템 리스트를 보여주는 Recycler View
        belongingItemRecyclerView = findViewById(R.id.belongingItemRecyclerView);
        // 소지품 리스트에 소지품아이템 하나를 추가하는 버튼
        belongingItemAddBtn = findViewById(R.id.belongingItemAddBtn);
        Intent intent = getIntent();
        if(!intent.getBooleanExtra("isAdd", true)){
            // 수정모드일 때.
            // 인덱스랑 belongSet정보가 담겨져있음.
            index = intent.getIntExtra("index", -1);
            belongingSetData forEditData = intent.getParcelableExtra("belongingSet");

            Log.i("BelongingSetAddActivity", "수정모드입니다. 수정할 소지품세트 정보:\n"+forEditData.toString());
            belongingItemList = new ArrayList<>();
            belongingItemList = forEditData.getBelongingList();
            belongingNameEditText.setText(forEditData.getName());
            belongingDetailEditText.setText(forEditData.getDetail());
            isChecked = forEditData.getSelected();
        }else{
            // 추가 모드일 때. default값
            belongingItemList = new ArrayList<>();
            index = -1;
            getSetItemData();
            isChecked = false;
        }
        belongingItemRecyclerAdapter = new BelongingItemRecyclerAdapter(belongingItemList);
        belongingItemRecyclerView.setAdapter(belongingItemRecyclerAdapter);
        belongingItemRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        belongingItemRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        belongingItemRecyclerAdapter.notifyDataSetChanged();
        belongingItemAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.create();
                nameEditText = new EditText(alertDialog.getContext());
                int dp10 = ConvertDPtoPX(alertDialog.getContext(), 10);
                nameEditText.setPadding(dp10, 0, dp10, 0);
                alertDialog
                        .setTitle("소지품 명을 입력해주세요.")
                        .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String belongingItemName = nameEditText.getText().toString();
                                Log.i("belongingItemAddDialog", "소지품 리스트에 '"+belongingItemName+"' 추가");
                                belongingItemRecyclerAdapter.addItem(belongingItemName);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("belongingItemAddDialog", "취소");
                                dialog.dismiss();
                            }
                        })
                        .setView(nameEditText);
                alertDialog.show();
            }
        });
        // 저장버튼. 소지품 세트 정보를 저장하고 이전 액티비티로 돌아갑니다.
        belongingSetAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setName = belongingNameEditText.getText().toString();
                String setDetail = belongingDetailEditText.getText().toString();
                ArrayList<String> setItemList = new ArrayList<>();
                setItemList = (ArrayList<String>) belongingItemRecyclerAdapter.getBelongingItemList().clone();
                belongingSetData newBelongingSet = new belongingSetData(setName, setDetail, setItemList);
                newBelongingSet.setSelected(isChecked);
                Log.i("belongingSetAddActivity", newBelongingSet.toString()+"\n을 추가합니다.");
                Intent intent = new Intent();
                intent.putExtra("belongingSet", newBelongingSet);
                intent.putExtra("index", index);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}