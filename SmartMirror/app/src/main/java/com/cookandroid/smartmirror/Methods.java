package com.cookandroid.smartmirror;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.widget.TextView;

public class Methods {
// Set AppBar Title Gradient Color
    public void setGradient(int startColor, int endColor, TextView tv){

        Shader textShader = new LinearGradient(0, 0, tv.getPaint().measureText(tv.getText().toString()), tv.getTextSize(),
                new int[]{startColor, endColor},
                new float[]{0,1}, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(textShader);
    }
}
