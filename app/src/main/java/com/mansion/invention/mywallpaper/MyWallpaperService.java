package com.mansion.invention.mywallpaper;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
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
        private boolean changeColor = false;
        private boolean touchEnabled;
        private int width;
        int height;

        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        public MyWallpaperEngine() {
            handler = new Handler();
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyWallpaperService.this);

//            touchEnabled = prefs.getBoolean("touch", true);
            paint.setAntiAlias(true);
            paint.setColor(myColor);
            paint.setStyle(Paint.Style.STROKE);
//            changeColor = true;
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


        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                changeColor = true;
                draw();
            }
            super.onTouchEvent(event);
        }


        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    if (myColor == Color.WHITE) myColor = Color.BLUE;
                    else myColor = Color.WHITE;
                    canvas.drawColor(myColor);
                }
            }finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            if (!changeColor) {
                handler.removeCallbacks(drawRunner);
                handler.postDelayed(drawRunner, 5000);
            }
            if (changeColor) changeColor = false;
        }
    }
}
