package com.cookandroid.smartmirror;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegisterDevActivity extends AppCompatActivity {
    customEditText editSerial;
    TextView tvTitle;
    Button btn;
    LinearLayout mainLayout;
    InputMethodManager manager;
    Toolbar toolbar;

    // When touch anything, EditText focus out and keyboard down
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if(view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")){
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if(x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()){
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
                editSerial.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_dev);

        // Add Coustom AppBar & Set Title Color Gradient
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = toolbar.findViewById(R.id.toolbarTv);
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);


        editSerial = findViewById(R.id.editSerial);
        btn = findViewById(R.id.testBtnRegister);
        mainLayout = findViewById(R.id.registerMainLayout);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LinkWifiActivity.class);
                startActivity(intent);
                finish();
            }
        });
        editSerial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_ENTER){
                        editSerial.clearFocus();
                        manager.hideSoftInputFromWindow(editSerial.getWindowToken(), 0);
                        return true;
                    }
                }
                return false;
            }
        });
    }
}