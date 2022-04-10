package com.cookandroid.smartmirror;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class customEditText extends androidx.appcompat.widget.AppCompatEditText {
    public customEditText(Context context) {
        super(context);
    }
    public customEditText(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            if(keyCode == KeyEvent.KEYCODE_BACK){
                this.clearFocus();
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
