package com.cookandroid.smartmirror;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.View;
import android.widget.TextView;

public class Methods {
// Set AppBar Title Gradient Color
    public void setGradient(int startColor, int endColor, TextView tv){

        Shader textShader = new LinearGradient(0, 0, tv.getPaint().measureText(tv.getText().toString()), tv.getTextSize(),
                new int[]{startColor, endColor},
                new float[]{0,1}, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(textShader);
    }
    // onclickListner에서 index를 전달하기 위한 함수

    public static int ConvertDPtoPX(Context context, int dp) { float density = context.getResources().getDisplayMetrics().density; return Math.round((float) dp * density); }

}
