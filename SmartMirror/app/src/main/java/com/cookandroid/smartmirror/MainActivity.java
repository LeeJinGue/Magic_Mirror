package com.cookandroid.smartmirror;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button connectBtn, btn;
    TextView sendTextView, readTextView, tvTitle;
    EditText sendEditText;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.testBtnMain);

        // Add Coustom AppBar & Set Title Color Gradient
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = toolbar.findViewById(R.id.toolbarTv);
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);


        // Move to next page
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterDevActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}