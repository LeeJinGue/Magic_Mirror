package com.cookandroid.smartmirror.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BelongingItemRecyclerAdapter extends RecyclerView.Adapter<BelongingItemRecyclerAdapter.ViewHolder> {
    ArrayList<String> belongingItemList = new ArrayList<>();

    // 아이템 뷰를 위한뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public BelongingItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.belonging_item, parent, false);
        BelongingItemRecyclerAdapter.ViewHolder vh = new BelongingItemRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시한다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name = belongingItemList.get(position);
        holder.index = holder.getAdapterPosition();
        holder.belongingItemNameTextView.setText(belongingItemList.get(position));
    }

    public BelongingItemRecyclerAdapter(ArrayList<String> belongingItemList){
        this.belongingItemList = belongingItemList;
    }
    public void addItem(String name){
        Log.i("BelongingRecyclerAdapter", name+"아이템 추가");
        belongingItemList.add(name);
        notifyItemInserted(belongingItemList.size());
        notifyItemRangeChanged(belongingItemList.size()-1, belongingItemList.size());
    }
    public String getItemName(int index){
        return belongingItemList.get(index);
    }
    public void removeAt(int index){
        Log.i("BelongingRecyclerAdapter", index+"번째 아이템 삭제");
        belongingItemList.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, belongingItemList.size());
    }

    @Override
    public int getItemCount() {
        return belongingItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        // 소지품 아이템 한 줄 한줄과 매칭시켜주는?
        private Context context;
        private String name;
        private TextView belongingItemNameTextView;
        private ImageView belongingItemAddBtn, belongingItemDelBtn;
        private EditText nameEditText;
        private int index;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            belongingItemNameTextView = (TextView) itemView.findViewById(R.id.belongingItemNameTextView);
            belongingItemAddBtn = (ImageView) itemView.findViewById(R.id.belongingItemAddBtn);
            belongingItemAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.create();
                    nameEditText = new EditText(alertDialog.getContext());
                    alertDialog
                            .setTitle("소지품 명을 입력해주세요.")
                            .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String belongingItemName = nameEditText.getText().toString();
                                    Log.i("belongingItemAddDialog", "소지품 리스트에 '"+belongingItemName+"' 추가");
                                    addItem(belongingItemName);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("belongingItemAddDialog", "취소");
                                    dialog.dismiss();
                                }
                            })
                            .setView(nameEditText);
                    alertDialog.show();
                }
            });
            belongingItemDelBtn = (ImageView) itemView.findViewById(R.id.belongingItemDelBtn);
            belongingItemDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    alertDialog.create();
                    alertDialog
                            .setTitle("정말 삭제 하시겠습니까?")
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("belongingItemDelDialog", index+"번째 아이템 "+"삭제");
                                    removeAt(index);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("belongingItemDelDialog", "취소");
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });

        }
    }
}
