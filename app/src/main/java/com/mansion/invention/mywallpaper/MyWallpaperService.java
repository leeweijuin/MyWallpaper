package com.mansion.invention.mywallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
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
        private GradientBackground gb;
        private int testCount, width;
        int height;


        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        public MyWallpaperEngine() {
            handler = new Handler();
            initBackgroundType();
            paint.setAntiAlias(true);
            paint.setColor(myColor);
            paint.setStyle(Paint.Style.STROKE);
            handler.post(drawRunner);
        }


        public void initBackgroundType() {
            gb = new ButterflyGradientBackground();
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

        public int getHour() {
            Calendar c = Calendar.getInstance();
            return testCount/60;
        }

        public int getMinute() {
            return testCount % 60;
        }


        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            if (testCount == 1440) testCount = 0;
            else testCount+=10;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.argb(gb.getAlpha(getHour(), getMinute()), 0, 0, 0));
                    int[] colors = gb.getCurrentGradientColor(getHour(), getMinute());

                    GradientDrawable grad = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
                    grad.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    grad.draw(canvas);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            handler.removeCallbacks(drawRunner);
            handler.postDelayed(drawRunner, 500);
        }

    }

}
