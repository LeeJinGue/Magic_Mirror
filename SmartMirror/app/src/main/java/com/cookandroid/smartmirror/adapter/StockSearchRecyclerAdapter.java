package com.cookandroid.smartmirror.adapter;

import android.content.Context;
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
import java.util.Locale;

public class StockSearchRecyclerAdapter extends RecyclerView.Adapter<StockSearchRecyclerAdapter.ViewHolder> {
    private ArrayList<String> stockSearchResult = new ArrayList<>();
    private ArrayList<String> arrayList;
    // 검색된 주식 결과 이름
    private String resultName;
    private onItemListener onItemListener;

    public void setOnItemListener(StockSearchRecyclerAdapter.onItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public StockSearchRecyclerAdapter(ArrayList<String> stockSearchResult){
        this.stockSearchResult = stockSearchResult;
        this.arrayList = new ArrayList<String>();
        this.arrayList.addAll(stockSearchResult);
    }
    public void addItem(String name){
        Log.i("StockSearchRecyclerAdapter", name+"아이템 추가");
        stockSearchResult.add(name);
        arrayList.add(name);
        notifyItemInserted(stockSearchResult.size());
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_stock_search, parent, false);
        StockSearchRecyclerAdapter.ViewHolder vh = new StockSearchRecyclerAdapter.ViewHolder(view);
        return vh;
    }
    public String getItem(int position){
        return stockSearchResult.get(position);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.index = holder.getAdapterPosition();
        holder.stockSearchItemTextView.setText(stockSearchResult.get(holder.getAdapterPosition()));
        resultName = stockSearchResult.get(holder.getAdapterPosition());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clickName = stockSearchResult.get(holder.getAdapterPosition());
                onItemListener.onItemClicked(clickName);
                Log.i("StockSearchRecyclerAdapter", "선택한 주식: "+clickName);

            }
        });

    }

    @Override
    public int getItemCount() {
        return stockSearchResult.size();
    }
    public void filter(String filter){
        filter = filter.toLowerCase(Locale.getDefault());
        stockSearchResult.clear();
        if(filter.length() == 0){
            Log.i("StockSearchRecyclerAdapter", "길이0");
            stockSearchResult.addAll(arrayList);

        }else{
            for(String sName : arrayList){
                if(sName.toLowerCase(Locale.getDefault()).contains(filter.toLowerCase(Locale.getDefault()))){
//                    Log.i("StockSearchRecyclerAdapter", "검색결과: "+sName);
                    stockSearchResult.add(sName);
                }
            }
        }
//        Log.i("StockSearchRecyclerAdapter", "결과리스트: "+stockSearchResult.toString());
        notifyItemRangeChanged(0, stockSearchResult.size());
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView stockSearchItemTextView;
        ImageView stockSearchBtn;
        int index;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stockSearchItemTextView = itemView.findViewById(R.id.stockSearchItemTextView);
            stockSearchBtn = itemView.findViewById(R.id.stockSearchBtn);

        }
    }
    public interface onItemListener{
        void onItemClicked(String name);
    }
}
