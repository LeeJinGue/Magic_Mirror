package com.cookandroid.smartmirror;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WifiAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater layoutInflater = null;
    ArrayList<wifiData> wifiData;
    public WifiAdapter(Context context, ArrayList<wifiData> data){
        mContext = context;
        wifiData = data;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return wifiData.size();
    }

    @Override
    public wifiData getItem(int position) {
        return wifiData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = layoutInflater.inflate(R.layout.wift_list_view, null);
        ImageView imgView = v.findViewById(R.id.wifiImg);
        TextView textView = v.findViewById(R.id.wifiName);
        imgView.setImageResource(wifiData.get(position).getImgSrc());
        textView.setText(wifiData.get(position).getWifiName());
        return v;
    }
}
