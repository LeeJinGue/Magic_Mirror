package com.cookandroid.smartmirror.navigationFragment;

import static com.cookandroid.smartmirror.Methods.ConvertDPtoPX;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.activities.AddAndSearchStockActivity;
import com.cookandroid.smartmirror.adapter.BelongingSetRecyclerAdapter;
import com.cookandroid.smartmirror.adapter.StockItemRecyclerAdapter;
import com.cookandroid.smartmirror.dataClass.belongingSetData;

import java.sql.Array;
import java.util.ArrayList;

public class StockSetupFragment extends Fragment {
    RecyclerView stockItemRecyclerView;
    Context context;
    ImageView stockAddBtn;
    ArrayList<String> stockList;
    EditText stockNameEditText;
    StockItemRecyclerAdapter mAdapter;
    String addedStockName;
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        addedStockName = intent.getStringExtra("stockName");
                        mAdapter.addItem(addedStockName);
                    }else{
                        addedStockName = "없음";
                    }
                }
            });
    void getUserStockList(){
        // DB에서 유저의 관심주식목록을 가져옵니다.
        mAdapter.addItem("주식1");
        mAdapter.addItem("주식2");
        mAdapter.addItem("주식3");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stock_setup, container, false);
        context = rootView.getContext();
        stockList = new ArrayList<>();
        mAdapter = new StockItemRecyclerAdapter(stockList);

        stockItemRecyclerView = rootView.findViewById(R.id.stockItemRecyclerView);
        stockItemRecyclerView.setAdapter(mAdapter);
        stockItemRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false));
        stockItemRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));

        stockAddBtn = rootView.findViewById(R.id.stockAddBtn);
        stockAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAndSearchStockActivity.class);
                mStartForResult.launch(intent);
            }
        });
        getUserStockList();


        return rootView;
    }
}
