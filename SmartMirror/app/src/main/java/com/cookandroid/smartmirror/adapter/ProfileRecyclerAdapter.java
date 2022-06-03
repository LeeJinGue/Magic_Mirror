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
import com.cookandroid.smartmirror.MirrorNetworkHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.activities.MainScreenActivity;
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
    MyApplication myApp;
    MirrorNetworkHelper networkHelper;
    public ProfileRecyclerAdapter(ArrayList<userData> mDataList, Context context,ActivityResultLauncher<Intent> StartForResultEditProfile, ActivityResultLauncher<Intent> StartForResultAddProfile ){
        this.StartForResultAddProfile = StartForResultAddProfile;
        this.StartForResultEditProfile = StartForResultEditProfile;
        this.mDataList = mDataList;
        this.context = context;
        this.sqlDB = new MirrorDBHelper(context, 1);
        this.networkHelper = sqlDB.getNetworkHelper();

        // 처음 생성할 때, 마지막 부분에 "추가하기"를 넣어둔다.
        myApp = (MyApplication) context.getApplicationContext();

    }
    public void addItem(userData newUser){
        // index랑 size는 1씩 차이가 나고 마지막엔 추가하기 버튼이므로 -2

        ArrayList<userData> newUserList = sqlDB.getAllUserList();
        for(userData u:newUserList) Log.i("addItem", "newUser: "+u.toString());
        // User Id는 Mirror Server에서 받아오므로 OK되면 추가합니다.
        // 테스트용으로 현재 전체 유저리스트 길이 +1로 아이디 지정해뒀음.
        MirrorNetworkHelper networkHelper = sqlDB.getNetworkHelper();
        String user_num_string = networkHelper.addUserToServer(newUser);
        try{
            int user_num = Integer.parseInt(user_num_string);
            newUser.setUser_num(user_num);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        sqlDB.addUser(newUser);
        // Server에서 OK받고 추가함.
        mDataList.add(mDataList.size()-1, newUser);


        notifyItemInserted(mDataList.size()-2);
    }
    public void editItemNameAt(userData editUser, int index){
        if(networkHelper.editUserToServer(editUser)){
            // 수정성공
            Log.i("editItemNameAt", "프로필 수정 성공");
            sqlDB.editUserName(editUser);
            mDataList.get(index).setName(editUser.getName());
            notifyItemChanged(index);
        }else{
            Log.i("editItemNameAt", "프로필 수정 실패");
            // 수정실패
        }
    }
    public void delItemAt(userData delUser,int index){
        if(networkHelper.delUserToServer(delUser)){
            Log.i("delItemAt", "프로필 삭제 성공");
            sqlDB.delUser(delUser);
            mDataList.remove(index);
            imageViewArrayList.remove(index);
            notifyItemRemoved(index);
        }else{
            Log.i("delItemAt", "프로필 삭제 실패");
        }

    }
    public void changeImgViewList(boolean isSettingMode){
        // 데이터가 없으면 바꾸지않는다.
        Log.i("ProfileRecyclerAdapter", "모드를 변경합니다. 이미지리스트 크기: "+imageViewArrayList.size());
        if(mDataList.size() == 1) return;
        if(isSettingMode){
            // 수정모드라면 프로필선택 모드로 바꾼다.
            for(int i =0; i< imageViewArrayList.size()-1; i++){
                ImageView imView = imageViewArrayList.get(i);
                imView.setImageResource(R.drawable.ic_baseline_image_24);
                imView.setTag("Select");
                Log.i("test", "프로필 선택 모드로 변경");
            }
        }else{
            // 프로필 선택 모드라면 수정모드로 바꾼다.
            for(int i =0; i< imageViewArrayList.size()-1; i++){
                ImageView imView = imageViewArrayList.get(i);
                imView.setImageResource(R.drawable.ic_baseline_offline_pin_24);
                imView.setTag("Edit");
                Log.i("test", "프로필 수정 모드로 변경");

            }
        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_profile, parent, false);
        ProfileRecyclerAdapter.ViewHolder vh = new ProfileRecyclerAdapter.ViewHolder(view);
        
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 마지막 인덱스는 추가하기 버튼으로
        Log.i("position&size Test", "position: "+holder.getAdapterPosition()+", size: "+mDataList.size());
        if(holder.getAdapterPosition() == mDataList.size()-1){
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
            imageViewArrayList.add(holder.profileItemImV);

        }else{
            // 마지막 인덱스가 아닐 경우
            holder.profileItemName.setText(mDataList.get(holder.getAdapterPosition()).getName());
            holder.profileItemImV.setTag("Select");
            holder.profileItemImV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("ProfileSelectRecyclerAdapter", "태그내용: "+holder.profileItemImV.getTag().toString());
                    if(holder.profileItemImV.getTag() == "Select"){
                        // 선택모드에서 클릭시 프로필 정보를 갖고 메인 화면으로 간다.
                        MyApplication myApp = (MyApplication)context.getApplicationContext();
                        myApp.setSelectedUser(mDataList.get(holder.getAdapterPosition()));
                        Intent intent = new Intent(context, MainScreenActivity.class);
                        intent.putExtra("profileData",mDataList.get(holder.getAdapterPosition()));
                        Log.i("ProfileSelectRecyclerAdapter", mDataList.get(holder.getAdapterPosition()).getName()+"가 선택되었습니다..");
                        myApp.setId(mDataList.get(holder.getAdapterPosition()).getUser_num());
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
            // 맨뒤에는 추가하기가 들어가야되므로 size-2의 인덱스부분에 추가한다.
            imageViewArrayList.add(holder.getAdapterPosition(),holder.profileItemImV);


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
