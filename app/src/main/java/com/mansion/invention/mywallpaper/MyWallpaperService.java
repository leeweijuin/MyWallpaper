package com.mansion.invention.mywallpaper;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.Calendar;

public class MyWallpaperService extends WallpaperService {
    public int myColor = Color.WHITE;

    public MyWallpaperService() {

    }


/*

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new IWallpaperServiceWrapper(this);
//        throw new UnsupportedOperationException("Not yet implemented");
    }
*/

    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends Engine {
        private final Handler handler; // = new Handler();
        private Paint paint = new Paint();
        private boolean visible = true;
        private int width, top,base, myAlpha;
        int height;


        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        public MyWallpaperEngine() {
            handler = new Handler();
            paint.setAntiAlias(true);
            paint.setColor(myColor);
            paint.setStyle(Paint.Style.STROKE);
            setCurrentColor();
            handler.post(drawRunner);
        }


        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }


        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }


        public void setCurrentColor() {
            Calendar c = Calendar.getInstance();
            int hourOfDay = Calendar.HOUR_OF_DAY;
            int minOfDay = Calendar.MINUTE;

            if (hourOfDay < 6) midnightDawn(hourOfDay, minOfDay);
            else if (hourOfDay < 9) dawnMorning(hourOfDay, minOfDay);
            else if (hourOfDay < 12) morningNoon(hourOfDay, minOfDay);
            else if (hourOfDay < 15) noonEvening(hourOfDay, minOfDay);
            else if (hourOfDay < 18) eveningNight(hourOfDay, minOfDay);
            else if (hourOfDay <= 23) nightMidnight(hourOfDay, minOfDay);
        }

        final int BLUE_COLOR = Color.argb(60, 83, 93, 176);
        final int DAWN_BLUE_COLOR = Color.argb(100, 62, 68, 125);
        final int DAWN_PURPLE_COLOR = Color.argb(100, 101,89, 137);
        final int NIGHT_PURPLE_COLOR = Color.argb(100, 139,96, 139);
        final int DAWN_PINK_COLOR = Color.argb(100, 222, 136, 165);
        final int NOON_PINK_COLOR = Color.argb(100, 238, 111, 178);
        final int CREAM_COLOR = Color.argb(100,255, 206, 187);

        public void nightMidnight(int hourOfDay, int minOfDay) {
            //target dark blue to darkblue , 6pm - midnight
            int targetTop = BLUE_COLOR;
            int targetBase = BLUE_COLOR;
            int targetAlpha = 20;

            int tt = 360;
            int ct = (hourOfDay - 18)*60 + minOfDay;
            top = calibrateColor(NIGHT_PURPLE_COLOR, BLUE_COLOR, tt, ct);
            base = calibrateColor(DAWN_BLUE_COLOR, BLUE_COLOR, tt, ct);
            myAlpha = 20;
        }

        public void midnightDawn(int hourOfDay, int minOfDay) {
            //target purple to dark blue, 00 - 6am
            int tt = 360;
            int ct = hourOfDay*60 + minOfDay;

            int targetTop = NIGHT_PURPLE_COLOR;
            int targetBase = DAWN_BLUE_COLOR;

            top = calibrateColor(BLUE_COLOR, NIGHT_PURPLE_COLOR, tt, ct);
            base = calibrateColor(BLUE_COLOR, DAWN_BLUE_COLOR, tt, ct);
            myAlpha = calibrateColor(0, 20, tt, ct);//0;
        }


        public void dawnMorning(int hourOfDay, int minOfDay) {
            // target pink to purple   (9am - 12noon)
            top = DAWN_PINK_COLOR;
            base = DAWN_PURPLE_COLOR;
//            myAlpha = 255;

            int tt = 180;
            int ct = (hourOfDay - 9)*60 + minOfDay;
            myAlpha = 255*ct/tt;
            top = calibrateColor(NIGHT_PURPLE_COLOR, DAWN_PINK_COLOR, tt, ct);
            base = calibrateColor(DAWN_BLUE_COLOR, DAWN_PURPLE_COLOR, tt, ct);
            myAlpha = calibrateColor(20, 255, tt, ct);
        }


        public void morningNoon(int hourOfDay, int minOfDay) {
            //target white to pink (12noon - 3pm)
            int tt = 180;
            int ct = (hourOfDay - 12)*60 + minOfDay;

            top = CREAM_COLOR;
            base = NOON_PINK_COLOR;
            top = calibrateColor(DAWN_PINK_COLOR, CREAM_COLOR, tt, ct);
            base = calibrateColor(DAWN_PURPLE_COLOR, NOON_PINK_COLOR, tt, ct);
            myAlpha = 255;
        }

        public void noonEvening(int hourOfDay, int minOfDay) {
            //target pink to purple (3pm - 6pm)
//            dawnMorning(hourOfDay, minOfDay);

            int tt = 180;
            int ct = (hourOfDay - 15)*60 + minOfDay;

            top = calibrateColor(CREAM_COLOR, DAWN_PINK_COLOR, tt, ct);
            base = calibrateColor(NOON_PINK_COLOR, DAWN_PURPLE_COLOR, tt, ct);
            myAlpha = 255;
        }

        public void eveningNight(int hourOfDay, int minOfDay) {
            //target purple to darkblue. (6pm - midnight)
//            midnightDawn(hourOfDay, minOfDay);

            int tt = 360;
            int ct = (hourOfDay - 18)*60 + minOfDay;

            int targetTop = NIGHT_PURPLE_COLOR;
            int targetBase = DAWN_BLUE_COLOR;

            top = calibrateColor(DAWN_PINK_COLOR, NIGHT_PURPLE_COLOR, tt, ct);
            base = calibrateColor(DAWN_PURPLE_COLOR, DAWN_BLUE_COLOR, tt, ct);
            myAlpha = calibrateColor(255, 20, tt, ct);//0;
        }


        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {

                    setCurrentColor();
                    int[] colors = {top, base};
                    canvas.drawColor(Color.argb(myAlpha, 255, 255, 255));
                    GradientDrawable grad = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);

//                    Log.d("opacity", String.valueOf(canvas.getMatrix()));
                    grad.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    grad.draw(canvas);
//                    updateCurrentColor();

                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            handler.removeCallbacks(drawRunner);
//            handler.postDelayed(drawRunner, 500);
        }


        /*
           * Update current color.
           */
/*
        private void updateCurrentColor() {

            if (repeatedApply < 30) {
                repeatedApply++;
            } else {
                night();
                if (currentRed != targetRed) currentRed = calibrateColor(currentRed, targetRed);
                if (currentBlue != targetBlue) currentBlue = calibrateColor(currentBlue, targetBlue);
                if (currentGreen != targetGreen) currentGreen = calibrateColor(currentGreen, targetGreen);

            }
        }
*/
        //TODO  :  calibrate alpha too!!
        public int calibrateColor(int previousColor, int targetColor, int tt, int ct) {
            if (tt - ct < 10) return targetColor;

            if (previousColor == targetColor) {
                return targetColor;
            } else if (previousColor < targetColor) {
                return previousColor + (targetColor - previousColor) /tt*ct;
            } else {
                return previousColor - (previousColor - targetColor) /tt*ct;
            }
        }


    }


}
