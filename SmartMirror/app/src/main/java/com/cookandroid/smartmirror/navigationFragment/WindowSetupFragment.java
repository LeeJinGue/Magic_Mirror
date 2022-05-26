package com.cookandroid.smartmirror.navigationFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.smartmirror.MirrorDBHelper;
import com.cookandroid.smartmirror.R;
import com.cookandroid.smartmirror.adapter.WindowAdapter;
import com.cookandroid.smartmirror.dataClass.MyApplication;
import com.cookandroid.smartmirror.dataClass.layoutData;
import com.cookandroid.smartmirror.dataClass.userData;

import java.util.ArrayList;
import java.util.Arrays;

public class WindowSetupFragment extends Fragment implements WindowAdapter.OnListItemLongSelectedInterface {
    private RecyclerView recyclerView;
    private WindowAdapter adapter;
    private ArrayList<layoutData> list = new ArrayList<layoutData>();
    private LinearLayout LinTopLeft, LinTopRight, LinBottomLeft, LinBottomRight;
    //private ItemTouchHelper helper;
    private boolean scrollStop;
    MyApplication myApp;
    MirrorDBHelper sqlDB;
    userData selectedUser;
    Button windowSetupBtn;
    ArrayList<layoutData> layoutSetList = new ArrayList<>();
    //
    String[] nowWidgetResTag;
    DragListener dragListener;
    LongCliskListnener longCliskListnener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_window_setup,container,false);
//        initUI(rootView);
        // DB Init
        sqlDB = new MirrorDBHelper(rootView.getContext(),2);
        myApp = (MyApplication) getActivity().getApplicationContext();
        // 현재 선택된 유저
        selectedUser = myApp.getSelectedUser();
//        layoutSetList = sqlDB.getLayoutDataListByUser(selectedUser);
        layoutSetList = sqlDB.getLayoutDataListByUser(selectedUser);
        sqlDB.setLayout_id(selectedUser.getUser_num());
        String dataset = "DB에서 불러온 레이아웃 데이터 리스트: \n";
        for(layoutData l: layoutSetList){
            dataset += l.toString()+"\n";
        }
        Log.i("DB_layout_test", dataset);
        // 적용버튼
        windowSetupBtn = rootView.findViewById(R.id.windowSetupBtn);
        //recyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        dragListener = new DragListener();
        longCliskListnener = new LongCliskListnener();

        //recyclerView.setOnLongClickListener(new LongClickListener());
        LinTopLeft = rootView.findViewById(R.id.topleft);
        LinTopRight = rootView.findViewById(R.id.topright);
        LinBottomLeft = rootView.findViewById(R.id.bottomleft);
        LinBottomRight = rootView.findViewById(R.id.bottomright);
        LinTopLeft.setOnDragListener(dragListener);
        LinTopRight.setOnDragListener(dragListener);
        LinBottomRight.setOnDragListener(dragListener);
        LinBottomLeft.setOnDragListener(dragListener);

        LinTopLeft.setOnLongClickListener(longCliskListnener);
        LinTopRight.setOnLongClickListener(longCliskListnener);
        LinBottomRight.setOnLongClickListener(longCliskListnener);
        LinBottomLeft.setOnLongClickListener(longCliskListnener);


        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(rootView.getContext(),2);
        //recyclerView.setLayoutManager(gridLayoutManager);
        WindowAdapter adapter = new WindowAdapter(rootView.getContext(),this);

        //icon 출처 : Flatcon
        // id, user_no, loc, type

        adapter.addItem(new layoutData(1, selectedUser.getUser_num(), 0, 0));
        adapter.addItem(new layoutData(1, selectedUser.getUser_num(), 0, 1));
        adapter.addItem(new layoutData(1, selectedUser.getUser_num(), 0, 2));
        adapter.addItem(new layoutData(1, selectedUser.getUser_num(), 0, 3));
        adapter.addItem(new layoutData(1, selectedUser.getUser_num(), 0, 4));
        adapter.addItem(new layoutData(1, selectedUser.getUser_num(), 0, 5));

        //        adapter.addItem(new layoutData("메시지확인", R.drawable.ic_message));
//        adapter.addItem(new layoutData("관심 주식 확인", R.drawable.ic_stock));
//        adapter.addItem(new layoutData("날씨", R.drawable.ic_weather));
//        adapter.addItem(new layoutData("소지품 확인", R.drawable.ic_belonging));
        //adapter.addItem(new Window("버스 도착시간 확인", R.drawable.ic_bus));

        recyclerView.setAdapter(adapter);

        windowSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 위젯에 어떤 res가 들어있는지 확인
                nowWidgetResTag = dragListener.getNowWidgetResTag();
                layoutSetList = new ArrayList<>();
                int loc = -1;
                int type = -1;
                // resTag를 돌면서
                for(int i=0; i< nowWidgetResTag.length; i++){
                    for(int j=0; j<layoutData.TYPE_RES_ID.length; j++){
                        //
                        if(Integer.parseInt(nowWidgetResTag[i]) == layoutData.TYPE_RES_ID[j]){
                            loc = i;
                            type = j;
                            layoutData newLayoutData = new layoutData(1, selectedUser.getUser_num(), loc,type);
                            Log.i("WindowSetupFragment", "새 layout data :\n"+newLayoutData.toString());
                            layoutSetList.add(newLayoutData);
                        }
                    }
                }
                sqlDB.layoutSet(layoutSetList,selectedUser);
            }
        });
        initUI(rootView, layoutSetList);
        return rootView;
    }
//    public layoutData getLayoutDataAndLoc(){
//
//    }
    private void initUI(ViewGroup rootView, ArrayList<layoutData> layoutSetList) {
        //DB로부터 사용자가 기존에 설정한 화면설정정보가 있다면, 읽어들여 초기화면 적용
        //없다면, default 화면
        String[] widgets = dragListener.getNowWidgetResTag();
        ImageView inittopleft = (ImageView)rootView.findViewById(R.id.imageView_topleft);
        ImageView inittopright = (ImageView)rootView.findViewById(R.id.imageView_topright);
        ImageView initbottomleft = (ImageView)rootView.findViewById(R.id.imageView_bottomleft);
        ImageView initbottomright = (ImageView)rootView.findViewById(R.id.imageView_bottomright);
        for(int i=0; i<layoutSetList.size();i++){
            layoutData nowLayout = layoutSetList.get(i);
            switch(nowLayout.getLoc()){
                case 0:
                    inittopleft.setImageResource(nowLayout.getImageResId());
                    inittopleft.setTag(String.valueOf(nowLayout.getImageResId()));
                    widgets[0] = String.valueOf(nowLayout.getImageResId());
                    Log.i("initUI", "왼쪽 위 레이아웃: "+nowLayout.toString());
                    break;
                case 1:
                    inittopright.setImageResource(nowLayout.getImageResId());
                    inittopright.setTag(String.valueOf(nowLayout.getImageResId()));
                    widgets[1] = String.valueOf(nowLayout.getImageResId());
                    Log.i("initUI", "오른쪽 위 레이아웃: "+nowLayout.toString());
                    break;
                case 2:
                    initbottomright.setImageResource(nowLayout.getImageResId());
                    initbottomright.setTag(String.valueOf(nowLayout.getImageResId()));
                    widgets[2] = String.valueOf(nowLayout.getImageResId());
                    Log.i("initUI", "오른쪽 아래 레이아웃: "+nowLayout.toString());
                    break;
                case 3:
                    initbottomleft.setImageResource(nowLayout.getImageResId());
                    initbottomleft.setTag(String.valueOf(nowLayout.getImageResId()));
                    widgets[3] = String.valueOf(nowLayout.getImageResId());
                    Log.i("initUI", "왼쪽 아래 레이아웃: "+nowLayout.toString());
                    break;
                default:
                    Log.i("initUI", "오류 레이아웃: "+nowLayout.toString());
                    break;
            }
        }
//        inittopleft.setImageResource(R.drawable.ic_calendar);
//        inittopright.setImageResource(R.drawable.ic_message);
//        initbottomleft.setImageResource(R.drawable.ic_calendar);
//        initbottomright.setImageResource(R.drawable.ic_stock);

    }
    // longClickListener를 통해 위젯이 삭제되었음을 알았을 때,
    // dragListener에게 삭제한 위젯의 인덱스를 알려주는 함수
    public void setNowWidgetResTagDel(int[] delIndex){
        String[] strings = dragListener.getNowWidgetResTag();
//        int[] delIndex = longCliskListnener.delIndex;
        for(int i: delIndex){
            if(i != -1){
                // strings의 i인덱스에 있는 값을 0으로 바꾼다.
                strings[i] = "0";
            }
        }
    }

    @Override
    public void onItemLongSelected(View v, int position) {

        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
        String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

        v.startDrag(data, // data to be dragged
                shadowBuilder, // drag shadow
                v, // 드래그 드랍할  View
                0 // 필요없은 플래그
        );

        v.setVisibility(View.VISIBLE);
    }
    class LongCliskListnener implements View.OnLongClickListener{
        int imageViewId, imageViewId2;
        ImageView currentImView, currentImView2;
        int[] bigWidgetResId = {R.drawable.ic_calendar, R.drawable.ic_weather};
        // 지울 화면의 인덱스. 2칸짜리일경우 2까지 있다.
//        int delIndex, delIndex2;
        int [] delIndex = {-1, -1};

        // 삭제할 인덱스를 리턴해줍니다.
        public int[] getDelIndex() {
            return delIndex;
        }

        @Override
        public boolean onLongClick(View v) {

            // LongClick한 View의 아이디에 따라 변경해야 할 이미지 아이디를 지정합니다.
            switch (v.getId()){
                case R.id.topleft:
                    Log.i("LongClickListener", "TopLeft 롱클릭");
                    imageViewId = R.id.imageView_topleft;
                    imageViewId2 = R.id.imageView_bottomleft;
                    delIndex[0] = 0;
                    delIndex[1] = 3;
                    break;
                case R.id.topright:
                    Log.i("LongClickListener", "TopRight 롱클릭");
                    imageViewId = R.id.imageView_topright;
                    imageViewId2 = R.id.imageView_bottomright;
                    delIndex[0] = 1;
                    delIndex[1] = 2;
                    break;

                case R.id.bottomleft:
                    Log.i("LongClickListener", "BottomLeft 롱클릭");
                    imageViewId = R.id.imageView_bottomleft;
                    imageViewId2 = R.id.imageView_topleft;
                    delIndex[0] = 3;
                    delIndex[1] = 0;
                    break;

                case R.id.bottomright:
                    Log.i("LongClickListener", "BottomRight 롱클릭");
                    imageViewId = R.id.imageView_bottomright;
                    imageViewId2 = R.id.imageView_topright;
                    delIndex[0] = 2;
                    delIndex[1] = 1;
                    break;
                default:
                    Log.i("LongClickListener", "롱클릭");
                    return true;

            }
            // 롱 클릭한 이미지뷰의 위/아래 이미지뷰를 가져오려면 부모 레이아웃을 통해 접근해야 합니다.
            View parentV = (View)v.getParent();
            // 롱 클릭한 이미지뷰와 위/아래 이미지뷰를 가져옵니다.
            currentImView = (ImageView) parentV.findViewById(imageViewId);
            currentImView2 = (ImageView) parentV.findViewById(imageViewId2);
            // 위젯이 비어있다면 삭제하지 않고 끝냅니다.
            if(Integer.parseInt(currentImView.getTag().toString()) == 0) return true;
            // 삭제 다이얼로그를 작성합니다.
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
            alertDialog.create();
            alertDialog
                    .setTitle("현재 화면의 위젯을 삭제하시겠습니까?")
                    .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(Integer.parseInt(currentImView.getTag().toString()) == Integer.parseInt(currentImView2.getTag().toString())){
                                // 선택한 위젯의 위/아래의 아이디가 같을 경우, 크기가 2인 위젯이므로 둘 다 비워줍니다.
                                Log.i("LongClickListener", "둘다삭제");
                                currentImView.setImageResource(0);
                                currentImView.setTag(0);
                                currentImView2.setImageResource(0);
                                currentImView2.setTag(0);
                                // 두칸짜리이므로 설정한대로 냅둡니다.
                            }else{
                                // 아닐 경우 롱클릭한 이미지뷰만 비워줍니다..
                                Log.i("LongClickListener", "하나삭제");
                                currentImView.setImageResource(0);
                                currentImView.setTag(0);
                                // 한칸짜리이므로 index의 두번째 값을 -1로 바꿉니다.
                                delIndex[1] = -1;
                            }
                            setNowWidgetResTagDel(delIndex);
                            dialog.dismiss();

                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("LongClickListener", "취소");
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return true;
        }
    }
    static class DragListener implements View.OnDragListener {
        // 드래그 되고있는 리소스아이디
        static int res_id = 0;
        // 선택된 위젯이 1칸짜리인지 2칸짜리인지 설정하는 변수
        static int square_cnt = 0;
        ArrayList<Integer> nowWidgetResIds = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0));
        int[] bigWidgetResId = {R.drawable.ic_calendar, R.drawable.ic_weather};
        private String[] nowWidgetResTag = {"0", "0", "0", "0"};
        public String[] getNowWidgetResTag() {
            return nowWidgetResTag;
        }

        @SuppressLint("ResourceType")
        @Override
        public boolean onDrag(View v, DragEvent e) {
            Drawable tempimg;
            // 위젯중복체크를 위한 위젯 이미지 리소스들
            // 일정확인, 메시지확인, 관심 주식 확인, 날씨, 소지품 확인, 없음(0) 순서

            // 위젯 중복체크를 위한 각 위치의 현재 리소스 아이디들(0이 빈거), topleft, topright, bottomleft, bottomright 순

            switch (e.getAction()) {
                // 드래그 시작될때
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d("DragClickListener", "ACTION_DRAG_STARTED");
                    View t = (View) e.getLocalState();
                    ImageView iv = (ImageView) t.findViewById(R.id.imageView);

                    tempimg = iv.getDrawable();
                    // 아이콘들 다 비트맵으로
                    Bitmap tempbitmap = ((BitmapDrawable)tempimg).getBitmap();
                    // 어떤 위젯을 드래그하는지 확인하고 그 위젯의 이미지를 res_id에 지정해줍니다.
                    // 2칸짜리 위젯인 경우 칸 변수를 2로, 1칸짜리 위젯인 경우 칸 변수를 1로 맞춰줍니다.
                    if (((BitmapDrawable) v.getResources().getDrawable(R.drawable.ic_calendar)).getBitmap().equals(tempbitmap)) {
                        res_id = R.drawable.ic_calendar;
                        square_cnt = 2;
                        return true;
                    } else if (((BitmapDrawable) v.getResources().getDrawable(R.drawable.ic_message)).getBitmap().equals(tempbitmap)) {
                        res_id = R.drawable.ic_message;
                        square_cnt = 1;
                        return true;
                    } else if (((BitmapDrawable) v.getResources().getDrawable(R.drawable.ic_stock)).getBitmap().equals(tempbitmap)) {
                        res_id = R.drawable.ic_stock;
                        square_cnt = 1;
                        return true;
                    } else if (((BitmapDrawable) v.getResources().getDrawable(R.drawable.ic_weather)).getBitmap().equals(tempbitmap)) {
                        res_id = R.drawable.ic_weather;
                        square_cnt = 2;
                        return true;
                    }else if (((BitmapDrawable) v.getResources().getDrawable(R.drawable.ic_belonging)).getBitmap().equals(tempbitmap)) {
                        res_id = R.drawable.ic_belonging;
                        square_cnt = 1;
                        return true;
                    }else if(((BitmapDrawable) v.getResources().getDrawable(R.drawable.ic_watch)).getBitmap().equals(tempbitmap)) {
                        res_id = R.drawable.ic_watch;
                        square_cnt = 1;
                        return true;
                    }else{
                        //bug
                        return true;

                    }

                case DragEvent.ACTION_DRAG_ENTERED:
                    // 드래그한 것을 옮길려는 지역으로 들어왔을때
                    Log.d("DragClickListener", "ACTION_DRAG_ENTERED");
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    // 드래그한 것이 영역을 빠져 나갈때
                    Log.d("DragClickListener", "ACTION_DRAG_EXITED");
                    return true;

                case DragEvent.ACTION_DROP:
                    // 드래그해서 드랍시켰을때
                    Log.d("DragClickListener", "ACTION_DROP");

                    View grandParentV = (View)v.getParent().getParent();
                    // 중복체크를 위해 네 영역의 현재 태그들을 갖고온다.(res_id)
                    nowWidgetResTag[0] = grandParentV.findViewById(R.id.imageView_topleft).getTag().toString();
                    nowWidgetResTag[1] = grandParentV.findViewById(R.id.imageView_topright).getTag().toString();
                    nowWidgetResTag[2] = grandParentV.findViewById(R.id.imageView_bottomright).getTag().toString();
                    nowWidgetResTag[3] = grandParentV.findViewById(R.id.imageView_bottomleft).getTag().toString();
                    // 드랍시켰을 때의 위치와 위/아래의 위치의 Id 및 Index를 받아오기 위해 변수를 선언합니다.
                    int viewId = 0;
                    int viewId2 = 0;
                    int viewIndex = -1;
                    int viewIndex2 = -1;
                    View parentV = (View) v.getParent();
                    // View Id를 찾는다.
                    if(v == v.findViewById(R.id.bottomright)){
                        viewId = R.id.imageView_bottomright;
                        viewId2 = R.id.imageView_topright;
                        viewIndex = 2;
                        viewIndex2 = 1;

                    }else if(v == v.findViewById(R.id.topright)){
                        viewId = R.id.imageView_topright;
                        viewId2 = R.id.imageView_bottomright;
                        viewIndex = 1;
                        viewIndex2 = 2;

                    }else if(v == v.findViewById(R.id.bottomleft)){
                        viewId = R.id.imageView_bottomleft;
                        viewId2 = R.id.imageView_topleft;
                        viewIndex = 3;
                        viewIndex2 = 0;
                    } else if(v == v.findViewById(R.id.topleft)){
                        viewId = R.id.imageView_topleft;
                        viewId2 = R.id.imageView_bottomleft;
                        viewIndex = 0;
                        viewIndex2 = 3;
                    }else{
                        // 못찾으면 그대로 나갑니다.
                        View view = (View) e.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        Toast.makeText(view.getContext(),
                                "이미지를 다른 지역에 드랍할수 없습니다.",
                                Toast.LENGTH_LONG).show();
                        return true;
                    }
                    View view = (View) e.getLocalState();
                    ViewGroup viewgroup = (ViewGroup) view
                            .getParent();
                    viewgroup.removeView(view);
                    // 포함되어있는 뷰를 찾아냅니다. 해당 뷰의 위/아래 뷰를 찾아내기 위해서는 parent를 통해 접근해야 합니다.
                    ImageView containView = (ImageView) parentV.findViewById(viewId);
                    ImageView containView2 = (ImageView) parentV.findViewById(viewId2);
                    if(square_cnt == 1){
                        // 드랍할 위젯이 1칸짜리일 때
                        for(int i=0; i<4; i++){
                            Log.i("중복체크", "res_id: "+res_id+", "+i+"번째 ResId: "+Integer.parseInt(nowWidgetResTag[i]));
                            if(i == viewIndex) continue;
                            if(Integer.parseInt(nowWidgetResTag[i]) == res_id){
                                Toast.makeText(v.getContext(), "중복된 위젯입니다.", Toast.LENGTH_SHORT).show();
                                view.setVisibility(View.VISIBLE);
                                return true;
                            }
                        }
                        // 드랍될 뷰에 채워져있는 이미지가 2칸짜리 이미지인지 확인합니다.
                        // 만약 2칸짜리라면 나머지 칸을 빈 이미지로 만듭니다.
                        if(Integer.parseInt(nowWidgetResTag[viewIndex]) == bigWidgetResId[0] ||
                                Integer.parseInt(nowWidgetResTag[viewIndex]) == bigWidgetResId[1]){
                            Log.i("2칸체크", "드랍될 뷰의 이미지가 2칸짜리입니다.");
                            containView2.setImageResource(0);
                            containView2.setTag(0);
                            // 나머지 칸을 비우면서 배열에도 0으로 저장합니다.
                            nowWidgetResTag[viewIndex2] = String.valueOf(0);
                        }

                        // 현재 화면에 배치된 위젯 상태를 저장합니다.
                        nowWidgetResTag[viewIndex] = String.valueOf(res_id);
                        Log.i("tagTest", "viewIndex: "+viewIndex+", viewIndex2: "+viewIndex2);
                        Log.i("valueTest", "nowWidgetResTag[viewIndex]: "+nowWidgetResTag[viewIndex]+", nowWidgetResTag[viewIndex2]: "+nowWidgetResTag[viewIndex2]);

                        containView.setImageResource(res_id);
                        containView.setTag(res_id);
                        view.setVisibility(View.VISIBLE);
                    }else if(square_cnt == 2){
                        // 드랍할 위젯이 2칸짜리일 때(세로 2칸임)
                         for(int i=0; i<2; i++){
                            // 선택될 위젯은 넘기고 나머지 부분에서 중복이 되는지 체크합니다.
                            if(i == viewIndex || i == viewIndex2) continue;
                            if(Integer.parseInt(nowWidgetResTag[i]) == res_id){
                                Toast.makeText(v.getContext(), "중복된 위젯입니다.", Toast.LENGTH_SHORT).show();
                                view.setVisibility(View.VISIBLE);
                                return true;
                            }
                        }

                        // 현재 화면에 배치된 위젯 상태를 저장합니다.
                        nowWidgetResTag[viewIndex] = String.valueOf(res_id);
                        nowWidgetResTag[viewIndex2] = String.valueOf(res_id);
//                        Log.i("tagTest", "viewIndex: "+viewIndex+", viewIndex2: "+viewIndex2);
//                        Log.i("valueTest", "nowWidgetResTag[viewIndex]: "+nowWidgetResTag[viewIndex]+", nowWidgetResTag[viewIndex2]: "+nowWidgetResTag[viewIndex2]);
                        containView.setImageResource(res_id);
                        containView.setTag(res_id);
                        containView2.setImageResource(res_id);
                        containView2.setTag(res_id);
                        view.setVisibility(View.VISIBLE);

                    }
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d("DragClickListener", "ACTION_DRAG_ENDED");
                    return true;

                default:
                    break;
            }
                return true;
        }



    }
}
