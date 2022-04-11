//package com.cookandroid.smartmirror;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.appcompat.widget.Toolbar;
//
//import org.w3c.dom.Attr;
//
//public class CustomAppbar extends Toolbar {
//    TextView textView;
//
//    public CustomAppbar(Context context){
//        super(context);
//        initView();
//    }
//    public void initView(){
//        String infService = Context.LAYOUT_INFLATER_SERVICE;
//        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
//        View v = li.inflate(R.layout.custom_appbar, this, false);
//        addView(v);
//        textView = (TextView) findViewById(R.id.toolbarTv);
//    }
//    public CustomAppbar(Context context, AttributeSet attrs){
//        super(context);
//        initView();
//        getAttrs(attrs);
//
//    }
//    public CustomAppbar(Context context, AttributeSet attrs, int defStyle){
//        super(context);
//        initView();
//        getAttrs(attrs, defStyle);
//    }
//
//    private void getAttrs(AttributeSet attrs){
//        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AppBar);
//        setTypeArray(typedArray);
//    }
//    private void getAttrs(AttributeSet attrs, int defStyle){
//        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AppBar, defStyle, 0);
//        setTypeArray(typedArray);
//    }
//
//    private void setTypeArray(TypedArray typedArray){
//        String text_string = typedArray.getString(R.styleable.AppBar_text);
//        textView.setText(text_string);
//        typedArray.recycle();
//    }
//
//}
