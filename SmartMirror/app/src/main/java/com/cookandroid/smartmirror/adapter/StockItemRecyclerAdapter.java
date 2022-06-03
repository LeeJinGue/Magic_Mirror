package com.cookandroid.smartmirror.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.MirrorDBHelper;
import com.cookandroid.smartmirror.MirrorNetworkHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.interestedStockData;

import java.util.ArrayList;

public class StockItemRecyclerAdapter extends RecyclerView.Adapter<StockItemRecyclerAdapter.ViewHolder> {
    ArrayList<interestedStockData> stockItemList = new ArrayList<>();
//    ArrayList<interestedStockData> stockDataList = new ArrayList<>();
    MirrorDBHelper sqlDB;
    MirrorNetworkHelper networkHelper;
    public StockItemRecyclerAdapter(ArrayList<interestedStockData> stockItemList, MirrorDBHelper sqlDB){
        this.stockItemList = stockItemList;
        this.sqlDB = sqlDB;
        this.networkHelper = new MirrorNetworkHelper();
    }
    @NonNull
    @Override
    public StockItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.stock_item, parent, false);
        StockItemRecyclerAdapter.ViewHolder vh = new StockItemRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StockItemRecyclerAdapter.ViewHolder holder, int position) {
        interestedStockData nowStock = stockItemList.get(holder.getAdapterPosition());
        holder.index = holder.getAdapterPosition();
        holder.stockName = nowStock.getStock_name();
        holder.stockItemNameTextView.setText(nowStock.getStock_name());
    }

    @Override
    public int getItemCount() {
        return stockItemList.size();
    }
    public void addItem(interestedStockData addStockData){
        for(interestedStockData i: stockItemList ){
            // 주식이름이 중복일 때
            if(i.getStock_name().equals(addStockData.getStock_name())){
                Log.i("StockItemRecyclerAdapter", addStockData+"는 중복입니다.");
                return;
            }
        }
        // 중복이 아닐 때
        Log.i("StockItemRecyclerAdapter", addStockData+"아이템 추가");
        String stock_id = networkHelper.addStockToServer(addStockData);
        addStockData.setStock_id(Integer.parseInt(stock_id));
        sqlDB.addInterestedStock(addStockData);
        stockItemList.add(addStockData);
        notifyItemInserted(stockItemList.size());
    }
    public void removeAt(int index) {
        Log.i("StockItemRecyclerAdapter", index + "번째 아이템 삭제");
        if(networkHelper.delStockToServer(stockItemList.get(index))){
            sqlDB.delInterestedStock(stockItemList.get(index));
            stockItemList.remove(index);
            notifyItemRemoved(index);
        }else{
            Log.i("StockItemRecyclerAdapter", "아이템 삭제 실패");
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private Context context;
        private String stockName;
        private ImageView stockItemDelBtn;
        private TextView stockItemNameTextView;
        private int index;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            stockItemDelBtn = itemView.findViewById(R.id.stockItemDelBtn);
            stockItemNameTextView = itemView.findViewById(R.id.stockItemNameTextView);
            stockItemDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    alertDialog.create();
                    alertDialog
                            .setTitle("정말 삭제 하시겠습니까?")
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("stockItemDelDialog", index+"번째 주식 "+"삭제");
                                    removeAt(index);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("stockItemDelDialog", "취소");
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
        }
    }
}
