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
        private boolean touchEnabled;
        private int width, top,base, myAlpha;
        int height;
        private int[] currentColor1;
        private int[] currentColor2;
//        private int[] targetColor1;
//        private int[] targetColor2;

        private int currentRed, currentGreen, currentBlue, targetRed, targetGreen, targetBlue;
        private boolean reverse;
        private int repeatedApply;
        private int testCounter;

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
            reverse = false;
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

        /*
           * Current color.
           */
        private int getCurrentColor() {
            return Color.argb(60, currentRed, currentGreen, currentBlue);
        }


        public void setCurrentColor() {
            Calendar c = Calendar.getInstance();
            int hourOfDay = Calendar.HOUR_OF_DAY;

            nightMidnight(c);
            /*if (hourOfDay < 6) midnightDawn(c);
            else if (hourOfDay < 9) dawnMorning(c);
            else if (hourOfDay < 12) morningNoon(c)
            else if (hourOfDay < 15) noonEvening(c);
            else if (hourOfDay < 18) eveningNight(c);
            else if (hourOfDay <= 23) nightMidnight(c);
       */ }


        public void midnightDawn(Calendar c) {
            //target purple to dark blue
            int hourOfDay = 5;//c.HOUR_OF_DAY;
            int minOfDay = 30; //c.MINUTE;
            int tt = 360;
            int ct = hourOfDay*60 + minOfDay;
            int targetTop = Color.argb(100, 139,96, 139);
            int targetBase = Color.argb(100, 62, 68, 125);
            top = targetTop;
            base = targetBase;
            myAlpha = 0;
        }


        public void dawnMorning(Calendar c) {
            top = Color.argb(100, 222, 136, 165);
            base = Color.argb(100, 101,89, 137);
            myAlpha = 255;
            // target pink to purple
        }

        public void morningNoon(Calendar c) {
            top = Color.argb(100,255, 206, 187);
            base = Color.argb(100, 238, 111, 178);
            myAlpha = 255;
            //target white to pink
        }

        public void noonEvening(Calendar c) {
            dawnMorning(c);
            //target pink to purple
        }

        public void eveningNight(Calendar c) {
            midnightDawn(c);
            //target purple to darkblue.
        }

        public void nightMidnight(Calendar c) {
            top = Color.argb(60, 83, 93, 176);
            base = Color.argb(60, 83, 93, 176);
            myAlpha = 20;
            //target dark blue to darkblue.
        }

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {

                    setCurrentColor();
                    int[] colors = {top, base};
//                    canvas.drawColor(17170447, PorterDuff.Mode.OVERLAY);
                    canvas.drawColor(Color.argb(myAlpha, 255, 255, 255));
                 GradientDrawable grad = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);

//                    Log.d("opacity", String.valueOf(canvas.getMatrix()));
                    grad.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                   // grad.setTintMode(PorterDuff.Mode.LIGHTEN);
//                    grad.setColorFilter(Color.argb(100, 252,243,234), PorterDuff.Mode.OVERLAY);
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
        public void night() {
            //base = //-16776961;
            targetRed = 42;
            targetGreen = 86;
            targetBlue = 152;
        }

        public void noon() {
            base = -1;
            targetRed = 255;
            targetGreen = 182;
            targetBlue = 153;
        }

        public void evening() {

            targetRed=192;
            targetGreen= 158;
            targetBlue = 205;
        }
*/

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

        public int calibrateColor(int currentColor, int targetColor) {
            int increment = 5;

            if (currentColor < targetColor) {
                if (currentColor + 5 > targetColor) currentColor = targetColor;
                else currentColor += increment;
            }else {
                if (currentColor - 5 < targetColor) currentColor = targetColor;
                else currentColor -= increment;
            }

            return currentColor;
        }
*/

    }


}
