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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.R;

import java.util.ArrayList;

public class StockItemRecyclerAdapter extends RecyclerView.Adapter<StockItemRecyclerAdapter.ViewHolder> {
    ArrayList<String> stockItemList = new ArrayList<>();
    public StockItemRecyclerAdapter(ArrayList<String> stockItemList){
        this.stockItemList = stockItemList;
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
        holder.index = holder.getAdapterPosition();
        holder.stockName = stockItemList.get(position);
        holder.stockItemNameTextView.setText(holder.stockName);
    }

    @Override
    public int getItemCount() {
        return stockItemList.size();
    }
    public void addItem(String name){
        Log.i("StockItemRecyclerAdapter", name+"아이템 추가");
        stockItemList.add(name);
        notifyItemInserted(stockItemList.size());
        notifyItemRangeChanged(stockItemList.size()-1, stockItemList.size());
        notifyDataSetChanged();
    }
    public String getItemName(int index){
        return stockItemList.get(index);
    }
    public void removeAt(int index){
        Log.i("StockItemRecyclerAdapter", index+"번째 아이템 삭제");
        stockItemList.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, stockItemList.size());
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
