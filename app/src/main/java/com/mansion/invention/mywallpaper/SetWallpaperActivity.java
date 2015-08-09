package com.mansion.invention.mywallpaper;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class SetWallpaperActivity extends Activity {
    private Button setWallpaperButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
/*
        startActivity(new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER));
        finish();
*/

        Intent intent = new Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, MyWallpaperService.class));
        startActivity(intent);

        finish();

/*
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, MyWallpaperService.class));
*/

//        startActivityForResult(intent, 10);
//        WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        startActivity(intent);
        /*setWallpaperButton = (Button) findViewById(R.id.btn_set_wallpaper);
        setWallpaperButton.setOnClickListener(setWallpaperListener);
   */ }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
//        Log.d("myLogTag", "the end");
        super.onActivityResult(requestCode, resultCode, data);
//        finish();

    }
/*

    private View.OnClickListener setWallpaperListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Toast toast = Toast.makeText(SetWallpaperActivity.this, R.string.toast_select_wallpaper, Toast.LENGTH_LONG);
            toast.show();

            startActivity(new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER));

            finish();

        }
    };
*/


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_set_wallpaper, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void onClick(View view) {
//        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
//        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
//                new ComponentName(this, MyWallpaperService.class));
//        startActivity(intent);
//    }
}
