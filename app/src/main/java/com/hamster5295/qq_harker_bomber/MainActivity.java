package com.hamster5295.qq_harker_bomber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import yanchan.Go;

public class MainActivity extends AppCompatActivity {

    private Go main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btn_Start = findViewById(R.id.SubmitBtn);
        TextView state = findViewById(R.id.stateText);
        TextView success = findViewById(R.id.successText);
        TextView fail = findViewById(R.id.failText);
        TextView log = findViewById(R.id.text_log);

        ScrollView s = findViewById(R.id.logView);

        @SuppressLint("HandlerLeak") Handler h = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                s.fullScroll(ScrollView.FOCUS_DOWN);
                switch (msg.what) {

                    case 0:
                        btn_Start.setText("停止轰炸");
                        state.setText("正在轰炸");
                        success.setText("成功数:" + main.getSuccessNumber());
                        fail.setText("失败数:" + main.getFailNumber());
                        if (SettingsActivity.setting == null) return;
                        if (SettingsActivity.setting.getBoolean("setting_log", false)) {
                            log.setText(main.getLog());
                            s.setVisibility(View.VISIBLE);
                        } else {
                            s.setVisibility(View.INVISIBLE);
                        }
                        break;

                    case 1:
                        s.setVisibility(View.INVISIBLE);
                        btn_Start.setText("开始轰炸");
                        state.setText("未开始");
                        success.setText("");
                        fail.setText("");
                        log.setText("");
                        break;
                }
            }
        };

        new Thread(() -> {
            while (true) {
                if (main == null) continue;
                h.sendEmptyMessage(main.isRunning() ? 0 : 1);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        btn_Start.setOnClickListener(view -> {
            if (main == null) {
                String url = ((EditText) findViewById(R.id.urlBox)).getText().toString();
                String userKey = ((EditText) findViewById(R.id.userKeyBox)).getText().toString();
                String passwordKey = ((EditText) findViewById(R.id.passwordKeyBox)).getText().toString();
                String dataw = ((EditText) findViewById(R.id.dataBox)).getText().toString();
                int count;
                try {
                    count = Integer.parseInt(((EditText) findViewById(R.id.numBox)).getText().toString());
                } catch (Exception e) {
                    count = 1;
                }

                if (url.equals("") || userKey.equals("") || passwordKey.equals("")) {
                    return;
                }

                HashMap<String, String> data = new HashMap<>();
                data.put("name", userKey);
                data.put("pass", passwordKey);
                data.put("data", dataw);
                main = new Go(url, new HashMap<String, String>(), count);
                main.setRandomData(true);
                main.start();

            } else if (!main.isRunning()) {
                int count;
                try {
                    count = Integer.parseInt(((EditText) findViewById(R.id.numBox)).getText().toString());
                } catch (Exception e) {
                    count = 1;
                }
                main.threadNumber = count;
                main.start();
            } else {
                main.stop();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenubar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        startActivity(new Intent(this, SettingsActivity.class));
        return true;


    }
}
