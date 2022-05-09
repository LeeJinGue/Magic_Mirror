package com.cookandroid.smartmirror.activities;

import static com.cookandroid.smartmirror.Methods.ConvertDPtoPX;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookandroid.smartmirror.Methods;
import com.cookandroid.smartmirror.R;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_belonging_set_add);
        context = getApplicationContext();
        // Add Coustom AppBar & Set Title Color Gradient
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbarTv);
        ImageView saveBtn = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams saveBtnParmas = new LinearLayout.LayoutParams(ConvertDPtoPX(context,24), ConvertDPtoPX(context,24));
        saveBtnParmas.gravity = Gravity.RIGHT;
        saveBtn.setImageResource(R.drawable.ic_save_24);
        saveBtn.setLayoutParams(saveBtnParmas);
        toolbar.addView(saveBtn);
        tvTitle.setText("소지품세트1");
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);
    }
}