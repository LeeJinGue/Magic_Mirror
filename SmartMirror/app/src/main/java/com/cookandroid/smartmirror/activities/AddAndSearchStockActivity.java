package com.cookandroid.smartmirror.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cookandroid.smartmirror.R;

public class AddAndSearchStockActivity extends AppCompatActivity {
    Button stockAddEndBtn;
    String stockName;
    EditText test2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_search_stock);
        stockAddEndBtn = findViewById(R.id.stockAddEndBtn);
        test2 = findViewById(R.id.test2);
        stockAddEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockName = test2.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("stockName", stockName);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}