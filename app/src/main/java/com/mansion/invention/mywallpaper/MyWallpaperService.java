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
        private int width, top, base, myAlpha, testCount;
        int height;

        final int BLUE_COLOR = Color.argb(60, 83, 93, 176);
        final int DAWN_BLUE_COLOR = Color.argb(100, 62, 68, 125);
        final int DAWN_PURPLE_COLOR = Color.argb(100, 101, 89, 137);
        final int NIGHT_PURPLE_COLOR = Color.argb(100, 139, 96, 139);
        final int DAWN_PINK_COLOR = Color.argb(100, 222, 136, 165);
        final int NOON_PINK_COLOR = Color.argb(100, 238, 111, 178);
        final int CREAM_COLOR = Color.argb(100, 255, 206, 187);

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
            int hourOfDay = testCount / 60; //Calendar.HOUR_OF_DAY;
            int minOfDay = testCount % 60;//Calendar.MINUTE;

            System.out.println("hour" + hourOfDay + "min" + minOfDay);
            if (hourOfDay < 6 || (hourOfDay == 6 && minOfDay == 0)) midnightDawn(hourOfDay, minOfDay);
            else if (hourOfDay < 9 || (hourOfDay == 9 && minOfDay == 0)) dawnMorning(hourOfDay, minOfDay);
            else if (hourOfDay < 12 || (hourOfDay == 12 && minOfDay == 0)) morningNoon(hourOfDay, minOfDay);
            else if (hourOfDay < 18 || (hourOfDay == 18 && minOfDay == 0)) noonEvening(hourOfDay, minOfDay);
            else if (hourOfDay < 21 || (hourOfDay == 21 && minOfDay == 0)) eveningNight(hourOfDay, minOfDay);
            else if (hourOfDay < 23 || (hourOfDay == 23 && minOfDay == 0)) nightMidnight(hourOfDay, minOfDay);
        }


        public void nightMidnight(int hourOfDay, int minOfDay) {
            //target dark blue to darkblue , 9pm - midnight
            int tt = 180;
            int ct = (hourOfDay - 18)* 60 + minOfDay;
            top = calibrateColor(NIGHT_PURPLE_COLOR, BLUE_COLOR, top, tt, ct);
            base = calibrateColor(DAWN_BLUE_COLOR, BLUE_COLOR, base, tt, ct);
            myAlpha = 100;
        }

        public void midnightDawn(int hourOfDay, int minOfDay) {
            //target purple to dark blue, 00 - 6am
            int tt = 360;
            int ct = hourOfDay * 60 + minOfDay;

            top = calibrateColor(BLUE_COLOR, NIGHT_PURPLE_COLOR, top, tt, ct);
            base = calibrateColor(BLUE_COLOR, DAWN_BLUE_COLOR, base, tt, ct);
            myAlpha = 100; //calibrateColor(0, 20, myAlpha,tt, ct);//0;
        }


        public void dawnMorning(int hourOfDay, int minOfDay) {
            // target pink to purple   (6am - 9am)
            int tt = 180;
            int ct = (hourOfDay - 6) * 60 + minOfDay;
            top = calibrateColor(NIGHT_PURPLE_COLOR, DAWN_PINK_COLOR, top, tt, ct);
            base = calibrateColor(DAWN_BLUE_COLOR, DAWN_PURPLE_COLOR, base,tt, ct);
            myAlpha = 70;//calibrateColor(20, 255, myAlpha, tt, ct);
        }


        public void morningNoon(int hourOfDay, int minOfDay) {
            //target white to pink (9am - 12noon)
            int tt = 180;
            int ct = (hourOfDay - 9) * 60 + minOfDay;
            top = calibrateColor(DAWN_PINK_COLOR, CREAM_COLOR, top, tt, ct);
            base = calibrateColor(DAWN_PURPLE_COLOR, NOON_PINK_COLOR, base,tt, ct);
            myAlpha = 0;
        }

        public void noonEvening(int hourOfDay, int minOfDay) {
            //target pink to purple (12noon - 6pm)
            int tt = 360;
            int ct = (hourOfDay - 12) * 60 + minOfDay;
            top = calibrateColor(CREAM_COLOR, DAWN_PINK_COLOR, top, tt, ct);
            base = calibrateColor(NOON_PINK_COLOR, DAWN_PURPLE_COLOR, base,tt, ct);
            myAlpha = 0;
        }

        public void eveningNight(int hourOfDay, int minOfDay) {
            //target purple to darkblue. (6pm - 9pm)
            int tt = 180;
            int ct = (hourOfDay - 15) * 60 + minOfDay;
            top = calibrateColor(DAWN_PINK_COLOR, NOON_PINK_COLOR, top, tt, ct);
            base = calibrateColor(DAWN_PURPLE_COLOR, DAWN_BLUE_COLOR, base, tt, ct);
            myAlpha = 0; //100; //calibrateColor(255, 20, myAlpha, tt, ct);//0;
        }


        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            if (testCount == 1440) testCount = 0;
            else testCount+=10;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.argb(myAlpha, 0, 0, 0));
                    setCurrentColor();
                    int[] colors = {top, base};

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
            handler.postDelayed(drawRunner, 500);
        }


        public int calibrateColor(int previousColor, int targetColor, int currentColor, int tt, int ct) {
            int redDiff = Math.abs(Color.red(targetColor) - Color.red(previousColor));
            int blueDiff = Math.abs(Color.blue(targetColor) - Color.blue(previousColor));
            int greenDiff = Math.abs(Color.green(targetColor) - Color.green(previousColor));
            int totalColorPoints = (redDiff + blueDiff + greenDiff);
            double timeFactor = ((double)ct/(double)tt);
            int increment =  (int)Math.floor(timeFactor * totalColorPoints);
            int newBlue = Color.blue(previousColor);
            int newGreen = Color.green(previousColor);
            int newRed = Color.red(previousColor);

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

}
