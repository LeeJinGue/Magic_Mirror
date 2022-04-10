package com.cookandroid.smartmirror;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class windowsetup extends Fragment implements WindowAdapter.OnListItemLongSelectedInterface{
    private RecyclerView recyclerView;
    private WindowAdapter adapter;
    private ArrayList<Window> list = new ArrayList<Window>();
    //private ItemTouchHelper helper;
    private boolean scrollStop;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.windowsetup,container,false);
        //initUI(rootView);

        //recyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);

        //recyclerView.setOnLongClickListener(new LongClickListener());
        rootView.findViewById(R.id.topleft).setOnDragListener(new DragListener());
        rootView.findViewById(R.id.topright).setOnDragListener(new DragListener());
        rootView.findViewById(R.id.bottmleft).setOnDragListener(new DragListener());
        rootView.findViewById(R.id.bottomright).setOnDragListener(new DragListener());

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(rootView.getContext(),2);
        //recyclerView.setLayoutManager(gridLayoutManager);
        WindowAdapter adapter = new WindowAdapter(rootView.getContext(),this);

        //icon 출처 : Flatcon
        adapter.addItem(new Window("일정확인", R.drawable.ic_calendar));
        adapter.addItem(new Window("메시지확인", R.drawable.ic_message));
        adapter.addItem(new Window("관심 주식 확인", R.drawable.ic_stock));
        adapter.addItem(new Window("날씨", R.drawable.ic_weather));
        adapter.addItem(new Window("버스 도착시간 확인", R.drawable.ic_bus));

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void initUI(ViewGroup rootView) {
        //DB로부터 사용자가 기존에 설정한 화면설정정보가 있다면, 읽어들여 초기화면 적용
        //없다면, default 화면
        ImageView inittopleft = (ImageView)rootView.findViewById(R.id.imageView_topleft);
        ImageView inittopright = (ImageView)rootView.findViewById(R.id.imageView_topright);
        ImageView initbottomleft = (ImageView)rootView.findViewById(R.id.imageView_bottomleft);
        ImageView initbottomright = (ImageView)rootView.findViewById(R.id.imageView_bottomright);

        //inittopleft.setImageResource(R.drawable.ic_calendar);
        //inittopright.setImageResource(R.drawable.ic_message);
        //initbottomleft.setImageResource(R.drawable.ic_weather);
        //initbottomright.setImageResource(R.drawable.ic_bus);

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

    static class DragListener implements View.OnDragListener {
        static int res_id = 0;
        @SuppressLint("ResourceType")
        @Override
        public boolean onDrag(View v, DragEvent e) {
            Drawable tempimg;
            switch (e.getAction()) {
                // 드래그 시작될때
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d("DragClickListener", "ACTION_DRAG_STARTED");
                    View t = (View) e.getLocalState();
                    ImageView iv = (ImageView) t.findViewById(R.id.imageView);

                    tempimg = iv.getDrawable();
                    Bitmap tempbitmap = ((BitmapDrawable)tempimg).getBitmap();

                    if (((BitmapDrawable) v.getResources().getDrawable(R.drawable.ic_calendar)).getBitmap().equals(tempbitmap)) {
                        res_id = R.drawable.ic_calendar;
                        return true;
                    } else if (((BitmapDrawable) v.getResources().getDrawable(R.drawable.ic_message)).getBitmap().equals(tempbitmap)) {
                        res_id = R.drawable.ic_message;
                        return true;
                    } else if (((BitmapDrawable) v.getResources().getDrawable(R.drawable.ic_stock)).getBitmap().equals(tempbitmap)) {
                        res_id = R.drawable.ic_stock;
                        return true;
                    } else if (((BitmapDrawable) v.getResources().getDrawable(R.drawable.ic_weather)).getBitmap().equals(tempbitmap)) {
                        res_id = R.drawable.ic_weather;
                        return true;
                    } else if (((BitmapDrawable) v.getResources().getDrawable(R.drawable.ic_bus)).getBitmap().equals(tempbitmap)) {
                        res_id = R.drawable.ic_bus;
                        return true;
                    } else {
                        //bug
                        return true;
                    }

                    // 드래그한 것을 옮길려는 지역으로 들어왔을때
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d("DragClickListener", "ACTION_DRAG_ENTERED");
                    // 이미지가 들어왔다는 것을 알려주기 위해 배경이미지 변경
                    return true;

                // 드래그한 것이 영역을 빠져 나갈때
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d("DragClickListener", "ACTION_DRAG_EXITED");
                    return true;

                // 드래그해서 드랍시켰을때
                case DragEvent.ACTION_DROP:
                    //View view = (View) e.getLocalState();
                    Log.d("DragClickListener", "ACTION_DROP");
                    if (v == v.findViewById(R.id.topleft)) {
                        View view = (View) e.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view
                                .getParent();
                        viewgroup.removeView(view);

                        ImageView containView = (ImageView) v.findViewById(R.id.imageView_topleft);

                        containView.setImageResource(res_id);
                        view.setVisibility(View.VISIBLE);
                        //Toast.makeText(v.getContext(), "topleft", Toast.LENGTH_LONG).show();
                    } else if (v == v.findViewById(R.id.topright)) {
                        View view = (View) e.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view
                                .getParent();
                        viewgroup.removeView(view);

                        ImageView containView = (ImageView) v.findViewById(R.id.imageView_topright);

                        containView.setImageResource(res_id);
                        view.setVisibility(View.VISIBLE);
                        //Toast.makeText(v.getContext(), "topright", Toast.LENGTH_LONG).show();
                    } else if (v == v.findViewById(R.id.bottmleft)) {
                        View view = (View) e.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view
                                .getParent();
                        viewgroup.removeView(view);

                        ImageView containView = (ImageView) v.findViewById(R.id.imageView_bottomleft);

                        containView.setImageResource(res_id);
                        view.setVisibility(View.VISIBLE);
                    } else if (v == v.findViewById(R.id.bottomright)) {
                        View view = (View) e.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view
                                .getParent();
                        viewgroup.removeView(view);

                        ImageView containView = (ImageView) v.findViewById(R.id.imageView_bottomright);

                        containView.setImageResource(res_id);
                        view.setVisibility(View.VISIBLE);
                    } else {
                        View view = (View) e.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        Toast.makeText(view.getContext(),
                                "이미지를 다른 지역에 드랍할수 없습니다.",
                                Toast.LENGTH_LONG).show();
                    }
                    /*
                    //Drag&Drop기능이 가능한 ItemView를 옮기는 기능
                    if (v == v.findViewById(R.id.topleft)) {
                        View view = (View) e.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view
                                .getParent();
                        viewgroup.removeView(view);
                        LinearLayout containView = (LinearLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);
                        Toast.makeText(v.getContext(), "topleft", Toast.LENGTH_LONG).show();
                    } else if (v == v.findViewById(R.id.topright)) {
                        View view = (View) e.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view
                                .getParent();
                        viewgroup.removeView(view);
                        LinearLayout containView = (LinearLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);
                        Toast.makeText(v.getContext(), "topright", Toast.LENGTH_LONG).show();
                    } else if (v == v.findViewById(R.id.bottmleft)) {
                        View view = (View) e.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view
                                .getParent();
                        viewgroup.removeView(view);
                        LinearLayout containView = (LinearLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);
                        Toast.makeText(v.getContext(), "bottomleft", Toast.LENGTH_LONG).show();
                    } else if (v == v.findViewById(R.id.bottomright)) {
                        View view = (View) e.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view
                                .getParent();
                        viewgroup.removeView(view);
                        LinearLayout containView = (LinearLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);
                        Toast.makeText(v.getContext(), "bottomright", Toast.LENGTH_LONG).show();
                    } else {
                        View view = (View) e.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        Toast.makeText(view.getContext(),
                                "이미지를 다른 지역에 드랍할수 없습니다.",
                                Toast.LENGTH_LONG).show();
                    }
                     */
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
