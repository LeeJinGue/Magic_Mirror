package com.cookandroid.smartmirror.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.activities.MainScreenActivity;
import com.cookandroid.smartmirror.activities.ProfileSelectActivity;
import com.cookandroid.smartmirror.activities.ProfileSettingActivity;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.userData;

import java.util.ArrayList;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.ViewHolder> {
    ArrayList<userData> mDataList = new ArrayList<>();
    ArrayList<ImageView> imageViewArrayList = new ArrayList<>();
    Context context;
    public ProfileRecyclerAdapter(ArrayList<userData> mDataList, Context context){
        this.mDataList = mDataList;
        this.context = context;
    }
    public void addItem(userData newUser){
        mDataList.add(newUser);
        notifyItemInserted(mDataList.size());
    }
    public void editItemNameAt(String name, int index){
        mDataList.get(index).setName(name);
        notifyItemChanged(index);
    }
    public void changeImgViewList(boolean isSettingMode, ActivityResultLauncher<Intent> StartForResultEditProfile ){

        if(isSettingMode){
            // 세팅모드라면 프로필선택 모드로 바꾼다.
            for(int i =0; i< imageViewArrayList.size(); i++){
                ImageView imView = imageViewArrayList.get(i);
                imView.setImageResource(R.drawable.ic_baseline_image_24);
                imView.setOnClickListener(new ProfileSelectActivity.OnClickListenerPutIndex(i) {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MainScreenActivity.class);
                        System.out.println(mDataList.get(index).getName()+"의 프로필이 선택되었습니다.");
                        context.startActivity(intent);
                    }
                });
            }
        }else{
            // 프로필 선택 모드라면 세팅모드로 바꾼다.
            for(int i =0; i< imageViewArrayList.size(); i++){
                ImageView imView = imageViewArrayList.get(i);
                imView.setImageResource(R.drawable.ic_baseline_offline_pin_24);
                imView.setOnClickListener(new ProfileSelectActivity.OnClickListenerPutIndex(i) {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ProfileSettingActivity.class);
                        System.out.println(mDataList.get(index).getName()+"의 프로필이 선택되었습니다.");
                        intent.putExtra("mode", "edit");
                        intent.putExtra("index", index);
                        intent.putExtra("editProfile", mDataList.get(index));
//                        context.startActivity(intent);
                        StartForResultEditProfile.launch(intent);
                    }
                });
            }
        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.profile_item, parent, false);
        ProfileRecyclerAdapter.ViewHolder vh = new ProfileRecyclerAdapter.ViewHolder(view);
        TextView test = new TextView(context);
        test.setText("테스트합니다.");
        parent.addView(test);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.profileItemName.setText(mDataList.get(holder.getAdapterPosition()).getName());
        imageViewArrayList.add(holder.profileItemImV);
        holder.profileItemImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainScreenActivity.class);
                System.out.println(mDataList.get(holder.getAdapterPosition()).getName()+"의 프로필이 선택되었습니다.");
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profileItemImV;
        TextView profileItemName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileItemImV = itemView.findViewById(R.id.profileItemImV);
            profileItemName = itemView.findViewById(R.id.profileItemName);
        }
    }
}
