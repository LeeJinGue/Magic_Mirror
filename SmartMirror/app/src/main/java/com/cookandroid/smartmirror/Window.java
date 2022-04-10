package com.cookandroid.smartmirror;

public class Window {
    private String screen;
    private int imageResId;

    public Window(String screen,int a_resId) {
        this.screen = screen;
        this.imageResId = a_resId;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public int getImageResId() {return imageResId;}

    public void setImageResId(int resId) {this.imageResId = resId;}
}
