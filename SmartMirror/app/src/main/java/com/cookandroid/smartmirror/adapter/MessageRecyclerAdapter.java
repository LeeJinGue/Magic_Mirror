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

import com.cookandroid.smartmirror.helper.MethodsHelper;
import com.cookandroid.smartmirror.helper.MirrorDBHelper;
import com.cookandroid.smartmirror.helper.MirrorNetworkHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.dataClass.messageData;

import java.util.ArrayList;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_DATE = 0;
    public static final int TYPE_MESSAGE = 1;
    ArrayList<messageData> messageAndDateList = new ArrayList<>();
    MirrorNetworkHelper networkHelper;
    MirrorDBHelper sqlDB;
    Context context;
    public MessageRecyclerAdapter(Context context, ArrayList<messageData> messageAndDateList){
        this.context = context;
        this.messageAndDateList = messageAndDateList;
        sqlDB = new MirrorDBHelper(context, 1);
        networkHelper = sqlDB.getNetworkHelper();
    }

    // ArrayList 관리
    public void addMessage(messageData newMsgData){
        String message_id = networkHelper.sendMessageToServer(newMsgData);
        newMsgData.setMessage_id(Integer.parseInt(message_id));
        sqlDB.addMessage(newMsgData);

        messageAndDateList.add(newMsgData);
        int lastIndex = messageAndDateList.size()-1;
        if(lastIndex == 0){
            // 첫 삽입이면 날짜객체 추가
            Log.i("errorTest", "lastIndex: "+lastIndex+", get(lastindex).getDate(): "+messageAndDateList.get(lastIndex).getDate());
            messageData firstMsgDate = new messageData(newMsgData.getDate());
            messageAndDateList.add(0, firstMsgDate);
            notifyItemRangeInserted(0,2);
        }else if(!MethodsHelper.getDateFromDateString(messageAndDateList.get(lastIndex).getDate())
                .equals(MethodsHelper.getDateFromDateString(messageAndDateList.get(lastIndex-1).getDate())) ){
            Log.i("errorTest", "lastIndex: "+lastIndex+", get(lastindex).getDate(): "+messageAndDateList.get(lastIndex).getDate()+", get(lastindex-1).getDate(): "+messageAndDateList.get(lastIndex-1).getDate());
            // 추가한 메시지의 날짜가 이전 메시지의 날짜와 같지 않다면 날짜객체를 추가해준다.
            messageData newMsgDateTime = new messageData(messageAndDateList.get(lastIndex).getDate());
            messageAndDateList.add(lastIndex, newMsgDateTime);
            notifyItemRangeInserted(lastIndex, 2);
        }else{
            notifyItemInserted(messageAndDateList.size());
        }
    }
    public void removeMessageAt(int index, messageData delMsgData){
        if(networkHelper.delMessageToServer(delMsgData)){
            sqlDB.delMessage(delMsgData);
            Log.i("MessageRecyclerAdapter", index+"번째 아이템 삭제, id: "+delMsgData.getMessage_id());
            messageAndDateList.remove(index);
            if(messageAndDateList.size()==1){
                // 지웠는데 하나밖에 안남았다? -> 날짜밖에 안남음. 지워야 한다.
                messageAndDateList.remove(0);
                notifyItemRangeRemoved(0, 2);
            }else if(messageAndDateList.size() == index){
                // 지웠는데 마지막 인덱스다?
                if(messageAndDateList.get(index-1).getViewType()==TYPE_DATE){
                    // + 이전 거가 날짜다? 이전 거도 같이지움.
                    messageAndDateList.remove(index-1);
                    notifyItemRangeRemoved(index-1, 2);
                }else{
                    // 아니면 그냥 하나만 지우고 끝
                    notifyItemRemoved(index);
                    return;
                }
            }else if(messageAndDateList.get(index).getViewType() == TYPE_DATE && messageAndDateList.get(index-1).getViewType()==TYPE_DATE){
                // 여러개가 남았는데, 지웠더니 내려온게 날짜고 index-1도 날짜면 index-1을 지운다.
                messageAndDateList.remove(index-1);
                notifyItemRangeRemoved(index-1,2);
            }else{
                notifyItemRemoved(index);
            }
        }
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
                view = inflater.inflate(R.layout.item_message_date, parent, false);
                MessageRecyclerAdapter.DateViewHolder dateViewHoldervh = new MessageRecyclerAdapter.DateViewHolder(view);
                return dateViewHoldervh;

            case R.integer.TYPE_MESSAGE_LEFT:
                view = inflater.inflate(R.layout.item_message_left, parent, false);
                MessageRecyclerAdapter.MessageLeftViewHolder messageLeftViewHolder = new MessageRecyclerAdapter.MessageLeftViewHolder(view);
                return messageLeftViewHolder;
            case R.integer.TYPE_MESSAGE_RIGHT:
                view = inflater.inflate(R.layout.item_message_right, parent, false);
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
                dateViewHolder.messageDateTextView.setText(msgData.getYear()+"년 "+msgData.getMonth()+"월 "+msgData.getDay()+"일");
                break;
            case R.integer.TYPE_MESSAGE_LEFT:
                MessageLeftViewHolder messageLeftViewHolder = (MessageLeftViewHolder) holder;
                messageLeftViewHolder.messageText.setText(msgData.getText());
                messageLeftViewHolder.messageTime.setText(msgData.getHourString()+":"+ msgData.getMinuteString());
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
                messageRightViewHolder.messageTime.setText(msgData.getHourString()+":"+ msgData.getMinuteString());
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
