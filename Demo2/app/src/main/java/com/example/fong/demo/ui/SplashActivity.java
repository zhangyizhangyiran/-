package com.example.fong.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.fong.demo.R;
import com.example.fong.demo.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private Timer timer;
    private ImageView img_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏设置，隐藏窗口所有装饰
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_aplash);
        img_splash = (ImageView) findViewById(R.id.img_splash);
        initview();
        startMainActivity();

    }



    private void startMainActivity() {
        //转向的Activity
        final Intent it = new Intent(this, MainActivity.class);
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //执行
                startActivity(it);
                finish();
            }
        };
        //3秒后
        timer.schedule(task, 3000);
    }

    /**
     * 加载splash图片
     */
    private void initview() {

        img_splash.setImageResource(R.drawable.pic_start);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
