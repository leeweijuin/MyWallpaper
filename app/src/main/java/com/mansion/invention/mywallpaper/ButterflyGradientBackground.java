package com.mansion.invention.mywallpaper;

import android.graphics.Color;


public class ButterflyGradientBackground extends GradientBackground {

    final int BLUE_COLOR = Color.argb(60, 83, 93, 176);
    final int DAWN_BLUE_COLOR = Color.argb(100, 62, 68, 125);
    final int DAWN_PURPLE_COLOR = Color.argb(100, 101, 89, 137);
    final int NIGHT_PURPLE_COLOR = Color.argb(100, 139, 96, 139);
    final int DAWN_PINK_COLOR = Color.argb(100, 222, 136, 165);
    final int NOON_PINK_COLOR = Color.argb(100, 238, 111, 178);
    final int CREAM_COLOR = Color.argb(100, 255, 206, 187);


    public ButterflyGradientBackground() {
    }


     public void getCurrentAlpha() {
        //night midnight  return 30;

    }


    public void midnightDawn(int hourOfDay, int minOfDay)  {
        //target purple to dark blue, 00 - 6am
        double tt = 360;
        double ct = hourOfDay * 60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(BLUE_COLOR, NIGHT_PURPLE_COLOR, top, timeFactor);
        base = calibrateColor(BLUE_COLOR, DAWN_BLUE_COLOR, base, timeFactor);
//        myAlpha = 100; //calibrateColor(0, 20, myAlpha,tt, ct);//0;
    }


    public void nightMidnight(int hourOfDay, int minOfDay) {
        //target dark blue to darkblue , 6pm - midnight
        double tt = 360;
        double ct = (hourOfDay - 18)* 60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(DAWN_PURPLE_COLOR, BLUE_COLOR, top, timeFactor);
        base = calibrateColor(DAWN_PURPLE_COLOR, BLUE_COLOR, base, timeFactor);
      //  myAlpha = 30;//calibrateColor(0, 100, myAlpha, timeFactor);//100;
    }



    public void dawnMorning(int hourOfDay, int minOfDay) {
        // target pink to purple   (6am - 9am)
        double tt = 180;
        double ct = (hourOfDay - 6)*60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(NIGHT_PURPLE_COLOR, DAWN_PINK_COLOR, top, timeFactor);
        base = calibrateColor(DAWN_BLUE_COLOR, DAWN_PURPLE_COLOR, base,timeFactor);
//        myAlpha = calibrateColor(100, 0, myAlpha, timeFactor);//calibrateColor(20, 255, myAlpha, tt, ct);
    }

    public void morningNoon(int hourOfDay, int minOfDay) {
        //target white to pink (9am - 12noon)
        double tt = 180;
        double ct = (hourOfDay - 9)*60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(DAWN_PINK_COLOR, CREAM_COLOR, top, timeFactor);
        base = calibrateColor(DAWN_PURPLE_COLOR, NOON_PINK_COLOR, base,timeFactor);
//        myAlpha = 0;
    }

    public void noonEvening(int hourOfDay, int minOfDay) {
        //target pink to purple (12noon - 3pm)
        double tt = 180;
        double ct = (hourOfDay - 12)*60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(CREAM_COLOR, CREAM_COLOR, top, timeFactor);
        base = calibrateColor(NOON_PINK_COLOR, DAWN_PINK_COLOR, base,timeFactor);
//        myAlpha = 0;
    }

    public void eveningNight(int hourOfDay, int minOfDay) {
        //target purple to darkblue. (3pm - 6pm)
        double tt = 180;
        double ct = (hourOfDay - 15)*60 + minOfDay;
        double timeFactor = ct/tt;

        top = calibrateColor(CREAM_COLOR, NOON_PINK_COLOR, top, timeFactor);
        base = calibrateColor(DAWN_PINK_COLOR, DAWN_PURPLE_COLOR, base, timeFactor);
//        myAlpha = 0;//calibrateColor(, 0, myAlpha, tt, ct);//0;
    }

}
