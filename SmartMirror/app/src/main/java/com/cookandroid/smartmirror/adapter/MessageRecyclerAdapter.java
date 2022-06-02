package com.cookandroid.smartmirror.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.MirrorDBHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.messageData;

import java.util.ArrayList;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_DATE = 0;
    public static final int TYPE_MESSAGE = 1;
    ArrayList<messageData> messageAndDateList = new ArrayList<>();
    MirrorDBHelper sqlDB;
    Context context;
    public MessageRecyclerAdapter(Context context, ArrayList<messageData> messageAndDateList){
        this.context = context;
        this.messageAndDateList = messageAndDateList;
        sqlDB = new MirrorDBHelper(context, 1);
    }

    // ArrayList 관리
    public void addMessage(messageData newMsgData){
        sqlDB.addMessage(newMsgData);
        messageAndDateList.add(newMsgData);
        notifyItemInserted(messageAndDateList.size());
    }
    public void removeMessageAt(int index, messageData delMsgData){
        sqlDB.delMessage(delMsgData);
        Log.i("StockItemRecyclerAdapter", index+"번째 아이템 삭제, id: "+delMsgData.getMessage_id());
        messageAndDateList.remove(index);
        notifyItemRemoved(index);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        // viewType에 따라 다른 View를 리턴합니다.
        switch (viewType){
            case R.integer.TYPE_DATE:
                view = inflater.inflate(R.layout.message_date_item, parent, false);
                MessageRecyclerAdapter.DateViewHolder dateViewHoldervh = new MessageRecyclerAdapter.DateViewHolder(view);
                return dateViewHoldervh;

            case R.integer.TYPE_MESSAGE_LEFT:
                view = inflater.inflate(R.layout.message_left_item, parent, false);
                MessageRecyclerAdapter.MessageLeftViewHolder messageLeftViewHolder = new MessageRecyclerAdapter.MessageLeftViewHolder(view);
                return messageLeftViewHolder;
            case R.integer.TYPE_MESSAGE_RIGHT:
                view = inflater.inflate(R.layout.message_right_item, parent, false);
                MessageRecyclerAdapter.MessageRightViewHolder messageRightViewHolder = new MessageRecyclerAdapter.MessageRightViewHolder(view);
                return messageRightViewHolder;
            default:
                Log.i("MessageRecyclerAdapter", "onCreateViewHolder ViewType 오류");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        messageData msgData = messageAndDateList.get(holder.getAdapterPosition());
        switch (holder.getItemViewType()){
            case R.integer.TYPE_DATE:
                // 날짜인 경우 날짜를 세팅합니다.
                DateViewHolder dateViewHolder = (DateViewHolder) holder;
                dateViewHolder.messageDateTextView.setText(msgData.getYear()+"년 "+msgData.getMonth()+"월 "+msgData.getDate()+"일");
                break;
            case R.integer.TYPE_MESSAGE_LEFT:
                MessageLeftViewHolder messageLeftViewHolder = (MessageLeftViewHolder) holder;
                messageLeftViewHolder.messageText.setText(msgData.getText());
                messageLeftViewHolder.messageTime.setText(msgData.getHour()+":"+ msgData.getMinute());
                break;
            case R.integer.TYPE_MESSAGE_RIGHT:
                MessageRightViewHolder messageRightViewHolder = (MessageRightViewHolder) holder;
                messageRightViewHolder.messageText.setText(msgData.getText());
                // 내가 보낸 메시지만 삭제가능
                messageRightViewHolder.messageText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                        alertDialog.create();
                        alertDialog
                                .setTitle("정말 삭제 하시겠습니까?")
                                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.i("messageDelDialog", holder.getAdapterPosition()+"번째 메시지 "+"삭제");
                                        removeMessageAt(holder.getAdapterPosition(), messageAndDateList.get(holder.getAdapterPosition()));
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.i("messageDelDialog", "취소");
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    }
                });
                messageRightViewHolder.messageTime.setText(msgData.getHour()+":"+ msgData.getMinute());
                break;
            default:
                Log.i("MessageRecyclerAdapter", "onBindViewHolder ViewType 오류");
                break;

        }
    }

    // 날짜인지, 왼쪽 메시지(받은 메시지)인지, 오른쪽 메시지(보낸 메시지)인지
    @Override
    public int getItemViewType(int position) {
        return messageAndDateList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return messageAndDateList.size();
    }

    public class DateViewHolder extends RecyclerView.ViewHolder{
        TextView messageDateTextView;
        public DateViewHolder(@NonNull View itemView) {
                super(itemView);
            messageDateTextView = itemView.findViewById(R.id.mesaageDateTextView);
            }
    }
    public class MessageLeftViewHolder extends RecyclerView.ViewHolder{
        TextView messageText, messageTime;
        public MessageLeftViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime = itemView.findViewById(R.id.messageTime);

        }
    }
    public class MessageRightViewHolder extends RecyclerView.ViewHolder{
        TextView messageText, messageTime;

        public MessageRightViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime = itemView.findViewById(R.id.messageTime);
        }
    }
}
