package com.anniemal.dbmz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.anniemal.dbmz.R;
import com.anniemal.utils.AnimationUtil;
import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SplashView;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;


public class SplashActivity extends Activity {

    private static final long DELAY_TIME = 3000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AdManager.getInstance(this).init("4d9b1b3f0f0995c3", "2a16d862b78b4bde", false);
        //redirectByTime();

        SharedPreferences setting = getSharedPreferences("isFirst", 0);
        Boolean user_first = setting.getBoolean("FIRST",true);
        if(user_first){//第一次
            redirectByTime();
            setting.edit().putBoolean("FIRST", false).commit();
        }else{
            SpotManager.getInstance(this).loadSpotAds();
            SplashView splashView = new SplashView(this, MainActivity.class);

            //开屏也可以作为控件加入到界面中。
            setContentView(splashView.getSplashView());

            SpotManager.getInstance(this).showSplashSpotAds(this, splashView,
                    new SpotDialogListener() {

                        @Override
                        public void onShowSuccess() {
                            Log.i("YoumiAdDemo", "开屏展示成功");
                        }

                        @Override
                        public void onShowFailed() {
                            Log.i("YoumiAdDemo", "开屏展示失败。");
                        }

                        @Override
                        public void onSpotClosed() {
                            Log.i("YoumiAdDemo", "开屏关闭。");
                        }
                    });
        }



//        // 实例化广告条
//        AdView adView = new AdView(this, AdSize.FIT_SCREEN);
//
//        // 获取要嵌入广告条的布局
//        LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
//
//        // 将广告条加入到布局中
//        adLayout.addView(adView);


    }

    private void redirectByTime() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                AnimationUtil.finishActivityAnimation(SplashActivity.this);
            }
        }, DELAY_TIME);
    }
}
