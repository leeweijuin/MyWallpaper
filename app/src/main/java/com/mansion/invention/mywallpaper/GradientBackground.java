package com.mansion.invention.mywallpaper;

import android.graphics.Color;

/**
 * Created by weiwei on 24/09/2015.
 */
public abstract class GradientBackground {

    public int top, base, myAlpha;


    /*
    * Constructor.
    */
    public GradientBackground() {
    }


    /*
    * Get current gradient color.
    */
    public int[] getCurrentGradientColor(int hourOfDay, int minOfDay) {

        System.out.println("hour" + hourOfDay + "min" + minOfDay);
        if (hourOfDay <= 6) midnightDawn(hourOfDay, minOfDay);
        else if (hourOfDay <= 9) dawnMorning(hourOfDay, minOfDay);
        else if (hourOfDay <= 12) morningNoon(hourOfDay, minOfDay);
        else if (hourOfDay <= 15) noonEvening(hourOfDay, minOfDay);
        else if (hourOfDay <= 18) eveningNight(hourOfDay, minOfDay);
        else if (hourOfDay <= 23) nightMidnight(hourOfDay, minOfDay);
        int[] gc = {top, base};

        return gc;
    }


    /*
    * Get alpha.
    */
    public int getAlpha(int hourOfDay, int minOfDay) {

        if (hourOfDay <= 6) {
            myAlpha= 100;
        } else if (hourOfDay <= 9) {
            double tt = 180;   //calibrateColor(100, 0, myAlpha, timeFactor);
            double ct = (hourOfDay - 6)*60 + minOfDay;
            double timeFactor = ct/tt;
            myAlpha = calibrateColor(100, 0, myAlpha, timeFactor);
        } else if (hourOfDay <= 12) {
            myAlpha = 0;
        } else if (hourOfDay <= 15) {
            myAlpha = 0;
        } else if (hourOfDay <= 18) {
            myAlpha = 0;
        } else if (hourOfDay <= 23) {
            myAlpha = 30;
        }
        return myAlpha;
    }


    /*
    * Night midnight.
    */
    public void nightMidnight(int hourOfDay, int minOfDay) {

    }


    /*
    * Midnight dawn.
    */
    public void midnightDawn(int hourOfDay, int minOfDay)  {

    }

    /*
    * Dawn morning.
    */
    public void dawnMorning(int hourOfDay, int minOfDay) {

    }


    /*
    * Morning noon.
    */
    public void morningNoon(int hourOfDay, int minOfDay) {
    }


    /*
    * Noon evening.
    */
    public void noonEvening(int hourOfDay, int minOfDay) {

    }


    /*
    * Evening night.
    */
    public void eveningNight(int hourOfDay, int minOfDay) {

    }


    /*
    * Calibrate color.
    */
    public int calibrateColor(int previousColor, int targetColor, int currentColor, double timeFactor) {
        int redDiff = Math.abs(Color.red(targetColor) - Color.red(previousColor));
        int blueDiff = Math.abs(Color.blue(targetColor) - Color.blue(previousColor));
        int greenDiff = Math.abs(Color.green(targetColor) - Color.green(previousColor));
        int newBlue = Color.blue(previousColor);
        int newGreen = Color.green(previousColor);
        int newRed = Color.red(previousColor);

        int increment =  (int)Math.floor(timeFactor * (redDiff + blueDiff + greenDiff));

        if (Color.red(currentColor) != Color.red(targetColor)) {
            newRed = calibrateSubColor(Color.red(previousColor), Color.red(targetColor),
                    Color.red(currentColor), increment);
        } else if (Color.red(currentColor) == Color.red(targetColor)){
            newRed = Color.red(targetColor);
        }

        if (Color.green(currentColor) != Color.green(targetColor) && (increment - redDiff) >= 0) {
            newGreen = calibrateSubColor(Color.green(previousColor), Color.green(targetColor),
                    Color.green(currentColor), Math.max(0, (increment - redDiff)));
        } else if (Color.green(currentColor) == Color.green(targetColor)){
            newGreen = Color.green(targetColor);
        }

        if (Color.blue(currentColor) != Color.blue(targetColor) && (increment - redDiff - greenDiff) >= 0) {
            newBlue = calibrateSubColor(Color.blue(previousColor), Color.blue(targetColor),
                    Color.blue(currentColor), Math.max(0, (increment - redDiff - greenDiff)));
        } else if (Color.blue(currentColor) == Color.blue(targetColor)){
            newBlue = Color.blue(targetColor);
        }
        return Color.argb(100, newRed, newGreen, newBlue);

    }


    /**
     * Calibrate sub color.
     */
    public int calibrateSubColor(int previousColor, int targetColor, int currentColor, int factor) {
        int newColor = previousColor;
        int increment = factor;

        if (previousColor < targetColor) {
            if (previousColor + increment < targetColor) newColor = previousColor + increment;
            else newColor = targetColor;

        } else if (previousColor > targetColor) {
            if (previousColor - increment > targetColor) newColor = previousColor - increment;
            else newColor = targetColor;
        }

        return newColor;
    }

}


