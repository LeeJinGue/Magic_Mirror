package com.cookandroid.smartmirror;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class WindowAdapter extends RecyclerView.Adapter<WindowAdapter.ViewHolder> {
        //implements ItemTouchHelperListener {
    private Context mContext;
    private static final String VIEW_TAG = "드래그 이미지";

    private ArrayList<Window> items = new ArrayList<Window>();

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    private static OnListItemLongSelectedInterface mLongListener;

    public WindowAdapter(Context context, OnListItemLongSelectedInterface LongListener) {
        this.mContext = context;
        this.mLongListener = LongListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.window_item, viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) {
        Window item = items.get(position);
        viewholder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /*
    @Override
    public boolean onItemMove(int from_position, int to_position) {
        Window w = items.get(from_position);
        items.remove(from_position);
        items.add(to_position,w);

        notifyItemMoved(from_position,to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }
    */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setTag(VIEW_TAG);
            textView = (TextView)itemView.findViewById(R.id.textView);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //int position = getAdapterPosition();
                    mLongListener.onItemLongSelected(v,getAdapterPosition());
                    return false;
                }
            });
        }
        
        public void setItem(Window item) {
            textView.setText(item.getScreen());
            imageView.setImageResource(item.getImageResId());
        }
    }

    public void addItem(Window item) {
        items.add(item);
    }

    public void setItems(ArrayList<Window> items) {
        this.items = items;
    }

    public Window getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Window item) {
        items.set(position,item);
    }
}
