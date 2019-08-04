package com.hamster5295.qq_harker_bomber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hamster5295.qq_harker_bomber.Bomber.Bomber;
import com.hamster5295.qq_harker_bomber.Bomber.BomberData;
import com.hamster5295.qq_harker_bomber.Bomber.yanchan.Go;
import com.hamster5295.qq_harker_bomber.Utils.SettingUtil;

public class PerformActivity extends AppCompatActivity {

    private Go go;
    private int index;
    private Bomber bomber;

    private CardView card_cmd;
    private LinearLayout layout_cmd;
    private EditText edit_threadCount;
    private FloatingActionButton fab_start;

    private TextView text_currentThreadCount;
    private TextView text_succeedCount;
    private TextView text_failCount;


    private boolean thread_update_flag = true;
    private Thread thread_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setResult(-1);

        if (getIntent() != null) {
            index = getIntent().getIntExtra("Index", 0);
        }
        bomber = BomberData.getBomber(index);


        card_cmd = findViewById(R.id.card_cmd);
        layout_cmd = findViewById(R.id.layout_cmd);
        edit_threadCount = findViewById(R.id.edit_threadCount);
        fab_start = findViewById(R.id.fab_start);

        text_currentThreadCount = findViewById(R.id.text_curretThreadCount);
        text_succeedCount = findViewById(R.id.text_succeedCount);
        text_failCount = findViewById(R.id.text_failCount);

        @SuppressLint("HandlerLeak")
        Handler uiHandler = new Handler() {


            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 0 && go != null) {
                    text_currentThreadCount.setText("当前线程数: " + go.getThreadNumberRealtime());
                    text_succeedCount.setText("成功数: " + go.getSuccessNumber());
                    text_failCount.setText("失败数: " + go.getFailNumber());
                    if (go.isRunning()) {
                        fab_start.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));
                    } else {
                        fab_start.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_start));

                    }

                    return;
                }

                TextView textView = new TextView(PerformActivity.this);

                if (msg.what == 3) {
                    textView.setTextColor(Color.RED);
                    textView.setText("链接超时了qwp\n");
                    layout_cmd.addView(textView);
                }

                if (msg.what == 4) {
                    Bundle data = msg.getData();
                    switch (msg.getData().getInt("code")) {
                        case 200:
                            textView.setTextColor(Color.GREEN);
                            textView.setText("Succeeded with code " + data.getInt("code") + " ,data: \n" + data.get("data") + "\n");

                            layout_cmd.addView(textView);
                            break;

                        case 302:
                            textView.setTextColor(Color.BLUE);
                            textView.setText("Succeeded with code " + data.getInt("code") + " ,data: \n" + data.get("data") + "\n");
                            layout_cmd.addView(textView);
                            break;

                        case 403:
                            textView.setTextColor(Color.RED);
                            textView.setText("Failed with code " + data.getInt("code") + ",\n兄dei你的连接被拒绝了，就像你表白女神一样\n");
                            layout_cmd.addView(textView);
                            break;

                        case 404:
                            textView.setTextColor(Color.RED);
                            textView.setText("Failed with code " + data.getInt("code") + ",哥们URL写错了\n");
                            layout_cmd.addView(textView);
                            break;

                        default:
                            textView.setTextColor(Color.RED);
                            textView.setText("Failed with code " + data.getInt("code") + " 未知错误qnq\n");
                            layout_cmd.addView(textView);
                            break;
                    }
                }

                Log.i("A", "handleMessage: ");

                if (layout_cmd.getChildCount() > 12)
                    layout_cmd.removeViews(0, 2);
            }
        };

        thread_update = new Thread(() -> {
            while (thread_update_flag) {
                uiHandler.sendEmptyMessage(0);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        go = new Go(bomber, uiHandler, 1);

        fab_start.setOnClickListener(view -> {
            if (go.isRunning()) {
                go.stop();
            } else {
                int i = 0;
                try {
                    i = Integer.parseInt(edit_threadCount.getText().toString());
                } catch (Exception e) {
                    i = 1;
                }

                if (i < 0) {
                    i = 1;
                }

                go = new Go(bomber, uiHandler, i);
                go.start();
            }
        });

        thread_update_flag = true;
        thread_update.start();

        card_cmd.setVisibility(SettingUtil.getSettingBoolean("setting_log") ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        thread_update_flag = false;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
