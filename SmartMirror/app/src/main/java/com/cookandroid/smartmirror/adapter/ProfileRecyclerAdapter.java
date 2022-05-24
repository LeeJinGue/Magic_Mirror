package com.cookandroid.smartmirror.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.MirrorDBHelper;
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
    ActivityResultLauncher<Intent> StartForResultEditProfile;
    ActivityResultLauncher<Intent> StartForResultAddProfile;
    Context context;
    MirrorDBHelper sqlDB;
    public ProfileRecyclerAdapter(ArrayList<userData> mDataList, Context context,ActivityResultLauncher<Intent> StartForResultEditProfile, ActivityResultLauncher<Intent> StartForResultAddProfile ){
        this.StartForResultAddProfile = StartForResultAddProfile;
        this.StartForResultEditProfile = StartForResultEditProfile;
        this.mDataList = mDataList;
        this.context = context;
        this.sqlDB = new MirrorDBHelper(context, 1);
        // 처음 생성할 때, 마지막 부분에 "추가하기"를 넣어둔다.
        userData last = new userData(2, -1, "추가하기", "없음");
        mDataList.add(last);
    }
    public void addItem(userData newUser){
        // index랑 size는 1씩 차이가 나고 마지막엔 추가하기 버튼이므로 -2

        ArrayList<userData> newUserList = sqlDB.getAllUserList();
        for(userData u:newUserList) Log.i("test", "newUser: "+u.toString());
        // User Id는 Mirror Server에서 받아오므로 OK되면 추가합니다.
        // 테스트용으로 현재 전체 유저리스트 길이 +1로 아이디 지정해뒀음.
        newUser.setUser_num(newUserList.size()+1);
        sqlDB.addUser(newUser);
        // Server에서 OK받고 추가함.
        mDataList.add(mDataList.size()-1, newUser);
        notifyItemInserted(mDataList.size()-2);
    }
    public void editItemNameAt(userData editUser, int index){
        mDataList.get(index).setName(editUser.getName());
        sqlDB.editUserName(editUser);
        notifyItemChanged(index);
    }
    public void delItemAt(int index){
        mDataList.remove(index);
        notifyItemChanged(index);
    }
    public void changeImgViewList(boolean isSettingMode){
        // 데이터가 없으면 바꾸지않는다.
        Log.i("ProfileRecyclerAdapter", "모드를 변경합니다.");
        if(mDataList.size() == 1) return;
        if(isSettingMode){
            // 수정모드라면 프로필선택 모드로 바꾼다.
            for(int i =0; i< imageViewArrayList.size(); i++){
                ImageView imView = imageViewArrayList.get(i);
                imView.setImageResource(R.drawable.ic_baseline_image_24);
                imView.setTag("Select");

            }
        }else{
            // 프로필 선택 모드라면 수정모드로 바꾼다.
            for(int i =0; i< imageViewArrayList.size(); i++){
                ImageView imView = imageViewArrayList.get(i);
                imView.setImageResource(R.drawable.ic_baseline_offline_pin_24);
                imView.setTag("Edit");

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
        
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 마지막 인덱스는 추가하기 버튼으로
        if(mDataList.size()==1){
            holder.profileItemName.setText("추가하기");
            holder.profileItemImV.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
            holder.profileItemImV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("ProfileSelectRecyclerAdapter", "프로필을 추가합니다.");
                    Intent intent = new Intent(context, ProfileSettingActivity.class);
                    intent.putExtra("mode", "add");
                    StartForResultAddProfile.launch(intent);
                }
            });
        }else{
            // 마지막 인덱스가 아닐 경우
            holder.profileItemName.setText(mDataList.get(holder.getAdapterPosition()).getName());
            imageViewArrayList.add(holder.profileItemImV);
            holder.profileItemImV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("ProfileSelectRecyclerAdapter", "태그내용: "+holder.profileItemImV.getTag().toString());
                    if(holder.profileItemImV.getTag() == "Select"){
                        // 선택모드에서 클릭시 프로필 정보를 갖고 메인 화면으로 간다.
                        Intent intent = new Intent(context, MainScreenActivity.class);
                        intent.putExtra("profileData",mDataList.get(holder.getAdapterPosition()));
                        Log.i("ProfileSelectRecyclerAdapter", mDataList.get(holder.getAdapterPosition()).getName()+"가 선택되었습니다..");
                        holder.context.startActivity(intent);
                    }else if(holder.profileItemImV.getTag() == "Edit"){
                        // 수정모드에서 클릭시 프로필 정보를 갖고 프로필세팅 화면으로 간다.
                        Intent intent = new Intent(context, ProfileSettingActivity.class);
                        intent.putExtra("editProfile",mDataList.get(holder.getAdapterPosition()));
                        intent.putExtra("mode", "edit");
                        intent.putExtra("index", holder.getAdapterPosition());
                        Log.i("ProfileSelectRecyclerAdapter", mDataList.get(holder.getAdapterPosition()).getName()+"의 프로필을 수정합니다.");
                        StartForResultEditProfile.launch(intent);
                    }else{
                        Log.i("ProfileSelectRecyclerAdapter", mDataList.get(holder.getAdapterPosition()).getName()+"의 태그 에러");
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profileItemImV;
        TextView profileItemName;
        Context context;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            profileItemImV = itemView.findViewById(R.id.profileItemImV);
            // 처음 생성될 때 태그는 Select다.
            profileItemImV.setTag("Select");
            profileItemName = itemView.findViewById(R.id.profileItemName);
        }
    }
}
