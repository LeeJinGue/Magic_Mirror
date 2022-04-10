package com.cookandroid.smartmirror;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LinkWifiActivity extends AppCompatActivity {
    ArrayList<wifiData> wifiData;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_wifi);
        btn = findViewById(R.id.testBtnLinkWifi);
        // Add Coustom AppBar & Set Title Color Gradient
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbarTv);
        Methods methods = new Methods();
        methods.setGradient(getColor(R.color.titleStart), getColor(R.color.titleEnd), tvTitle);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);

        getWifiList();

        ListView listView = (ListView) findViewById(R.id.wifiListView);
        final WifiAdapter wifiAdapter = new WifiAdapter(this, wifiData);

        listView.setAdapter(wifiAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Log.i("test", "와이파이명: "+wifiAdapter.getItem(position).getWifiName());
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public void getWifiList(){
        wifiData = new ArrayList<wifiData>();
        wifiData.add(new wifiData(R.drawable.ic_baseline_wifi_lock_24, "KT_5G_12345"));
        wifiData.add(new wifiData(R.drawable.ic_baseline_wifi_lock_24, "KT_12345"));
        wifiData.add(new wifiData(R.drawable.ic_baseline_wifi_lock_24, "LG_5G"));


    }

}