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
        private int width, base;
        int height;
        private int[] currentColor1;
        private int[] currentColor2;
        private int[] targetColor1;
        private int[] targetColor2;

        private int currentRed, currentGreen, currentBlue, targetRed, targetGreen, targetBlue;
        private boolean reverse;
        private int repeatedApply;

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
            currentRed = 255;
            currentGreen = 182;
            currentBlue = 193;
            targetRed = 255;
            targetGreen = 182;
            targetBlue = 193;

            reverse = false;
            base = -1;
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

        }


        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(0xFFF3F3F3);
                    setCurrentColor();
                    int[] colors = {base, getCurrentColor()};
                    GradientDrawable grad = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);

//                    Log.d("opacity", String.valueOf(canvas.getMatrix()));
                    grad.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//                    grad.setTintMode(PorterDuff.Mode.DARKEN);
//                    grad.setColorFilter(0x63478C, PorterDuff.Mode.XOR);
                    grad.draw(canvas);
//                    updateCurrentColor();

                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            handler.removeCallbacks(drawRunner);
            handler.postDelayed(drawRunner, 500);
        }


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

        /*
           * Update current color.
           */
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

    }


}
