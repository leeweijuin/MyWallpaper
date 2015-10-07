package com.mansion.invention.mywallpaper;

import android.graphics.Color;

/**
 * Created by weiwei on 24/09/2015.
 */
public abstract class gradientBackground {

    public int width, top, base, myAlpha, testCount;

    public gradientBackground() {
    }


    public void nightMidnight(int hourOfDay, int minOfDay) {

    }


    public void midnightDawn(int hourOfDay, int minOfDay)  {

    }

    public void dawnMorning(int hourOfDay, int minOfDay) {

    }

    public void morningNoon(int hourOfDay, int minOfDay) {
    }

    public void noonEvening(int hourOfDay, int minOfDay) {

    }

    public void eveningNight(int hourOfDay, int minOfDay) {

    }


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
     *
     * @param previousColor
     * @param targetColor
     * @param currentColor
     * @param factor
     * @return
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


