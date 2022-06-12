package com.cookandroid.smartmirror.activities;

import static com.cookandroid.smartmirror.helper.MethodsHelper.ConvertDPtoPX;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookandroid.smartmirror.helper.MethodsHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.adapter.BelongingItemRecyclerAdapter;
import com.cookandroid.smartmirror.dataClass.MyApplication;
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
    EditText belongingNameEditText, belongingInfoEditText;
    Button belongingSetAddBtn;
    ImageView belongingItemAddBtn;
    RecyclerView belongingItemRecyclerView;
    BelongingItemRecyclerAdapter belongingItemRecyclerAdapter;
    boolean isActiavted, isAdd;
    // 수정할 / 추가할 소지품 세트의 인덱스
    int index;
    MyApplication myApp;
    ArrayList<String> belongingItemList;
    belongingSetData forEditData;
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
        MethodsHelper methodsHelper = new MethodsHelper();
        methodsHelper.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);

        // 선택된 유저 등 전역변수를 관리하는 객체
        myApp = (MyApplication)getApplicationContext();

        // 소지품 세트의 이름을 입력하는 EditText
        belongingNameEditText = findViewById(R.id.belongingSetNameEditText);
        // 소지품 세트의 상세설명을 입력하는 EditText
        belongingInfoEditText = findViewById(R.id.belongingSetInfoEditText);
        // 소지품 세트를 추가하는(현재까지 입력한 소지품들을 저장하는) 버튼
        belongingSetAddBtn = findViewById(R.id.belongingSetAddBtn);
        // 현재 소지품 세트의 소지품 아이템 리스트를 보여주는 Recycler View
        belongingItemRecyclerView = findViewById(R.id.belongingItemRecyclerView);
        // 소지품 리스트에 소지품아이템 하나를 추가하는 버튼
        belongingItemAddBtn = findViewById(R.id.belongingItemAddBtn);
        Intent intent = getIntent();
        isAdd =intent.getBooleanExtra("isAdd", true);
        if(!isAdd){
            // 수정모드일 때.
            // 인덱스랑 belongSet정보가 담겨져있음.
            index = intent.getIntExtra("index", -1);
            forEditData = intent.getParcelableExtra("belongingSet");

            Log.i("BelongingSetAddActivity", "수정모드입니다. 수정할 소지품세트 정보:\n"+forEditData.toString());
            belongingItemList = MethodsHelper.getStuffArrayListFromString(forEditData.getStuff_list_str());
            belongingNameEditText.setText(forEditData.getSet_name());
            belongingInfoEditText.setText(forEditData.getSet_info());
            isActiavted = forEditData.isActiavted();
            tvTitle.setText("소지품세트 수정");
        }else{
            // 추가 모드일 때. default값
            belongingItemList = new ArrayList<>();
            index = -1;
//            getSetItemData();
            isActiavted = false;
        }
        belongingItemRecyclerAdapter = new BelongingItemRecyclerAdapter(belongingItemList);
        belongingItemRecyclerView.setAdapter(belongingItemRecyclerAdapter);
        belongingItemRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        belongingItemRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        belongingItemAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.create();
                nameEditText = new EditText(alertDialog.getContext());
                int paddingDp = ConvertDPtoPX(alertDialog.getContext(), 10);
                nameEditText.setPadding(paddingDp, 0, paddingDp, 0);
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
                String setInfo = belongingInfoEditText.getText().toString();
                ArrayList<String> stuffList_arr = new ArrayList<>();
                stuffList_arr = (ArrayList<String>) belongingItemRecyclerAdapter.getBelongingItemList().clone();
                String stuff_list_str = MethodsHelper.getStringFromStuffArrayList(stuffList_arr);
                if(isAdd){
                    // 저장모드
                    belongingSetData newBelongingSet = new belongingSetData(1, myApp.getSelectedUser().getUser_num(), setName,"0", setInfo, stuff_list_str);
                    newBelongingSet.setActiavted(isActiavted);

                    Log.i("belongingSetAddActivity", newBelongingSet.toString()+"\n을 추가합니다.");
                    Intent intent = new Intent();
                    intent.putExtra("belongingSet", newBelongingSet);
                    intent.putExtra("index", index);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    // 수정모드
                    belongingSetData editBelongingSet = new belongingSetData(forEditData.getBelonging_id(), myApp.getSelectedUser().getUser_num(), setName,forEditData.getActivation(), setInfo, stuff_list_str);
                    Intent intent = new Intent();
                    intent.putExtra("belongingSet", editBelongingSet);
                    intent.putExtra("index", index);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });

    }
}