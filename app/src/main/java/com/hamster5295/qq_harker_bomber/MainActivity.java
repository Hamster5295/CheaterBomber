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
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;

import yanchan.Go;

public class MainActivity extends AppCompatActivity {

    private Go main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SettingsActivity.setting = PreferenceManager.getDefaultSharedPreferences();
        SettingUtil.setting = PreferenceManager.getDefaultSharedPreferences(this);

        final Button btn_Start = findViewById(R.id.SubmitBtn);

        TextView textView_prefix = findViewById(R.id.text_prefix);
        EditText editText_prefix = findViewById(R.id.prefixBox);

        TextView textView_extra = findViewById(R.id.text_extra);
        EditText editText_extra = findViewById(R.id.extraBox);

        TextView state = findViewById(R.id.stateText);
        TextView success = findViewById(R.id.successText);
        TextView fail = findViewById(R.id.failText);
        TextView log = findViewById(R.id.text_log);

        ScrollView s = findViewById(R.id.logView);

        @SuppressLint("HandlerLeak") Handler h = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (SettingUtil.getSettingBoolean("setting_log"))
                    s.fullScroll(ScrollView.FOCUS_DOWN);

                if (SettingUtil.getSettingBoolean("setting_get")) {
                    editText_prefix.setVisibility(View.VISIBLE);
                    textView_prefix.setVisibility(View.VISIBLE);
                    editText_extra.setVisibility(View.GONE);
                    textView_extra.setVisibility(View.GONE);
                } else {
                    editText_prefix.setVisibility(View.GONE);
                    textView_prefix.setVisibility(View.GONE);
                    editText_extra.setVisibility(View.VISIBLE);
                    textView_extra.setVisibility(View.VISIBLE);
                }

                switch (msg.what) {

                    case 0:
                        btn_Start.setText("停止轰炸");
                        state.setText("正在轰炸");
                        success.setText("成功数:" + main.getSuccessNumber());
                        fail.setText("失败数:" + main.getFailNumber());

                        if (SettingUtil.getSettingBoolean("setting_log")) {
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
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (main == null) {
                    h.sendEmptyMessage(1);
                    continue;
                }
                h.sendEmptyMessage(main.isRunning() ? 0 : 1);

            }
        }).start();


        btn_Start.setOnClickListener(view -> {
            if (main == null) {
                if (SettingUtil.getSettingBoolean("setting_get")) {
                    initGet();
                } else {
                    initPush();
                }

            } else if (!main.isRunning()) {
                if (SettingUtil.getSettingBoolean("setting_get")) {
                    initGet();
                } else {
                    initPush();
                }
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

    public void initPush() {
        String url = ((EditText) findViewById(R.id.urlBox)).getText().toString();
        String userKey = ((EditText) findViewById(R.id.userKeyBox)).getText().toString();
        String passwordKey = ((EditText) findViewById(R.id.passwordKeyBox)).getText().toString();
        String dataw = ((EditText) findViewById(R.id.extraBox)).getText().toString();
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
        main = new Go(url, data, count);
        main.setRandomData(true);
        main.start(false);
    }

    public void initGet() {
        String url = ((EditText) findViewById(R.id.urlBox)).getText().toString();
        String userKey = ((EditText) findViewById(R.id.userKeyBox)).getText().toString();
        String passwordKey = ((EditText) findViewById(R.id.passwordKeyBox)).getText().toString();
        String prefix = ((EditText) findViewById(R.id.prefixBox)).getText().toString();
        int count;
        try {
            count = Integer.parseInt(((EditText) findViewById(R.id.numBox)).getText().toString());
        } catch (Exception e) {
            count = 1;
        }

        if (url.equals("") || userKey.equals("") || passwordKey.equals("")) {
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("name", userKey);
        map.put("pass", passwordKey);
        map.put("prefix", prefix);

        main = new Go(url, map, count);
        main.setRandomData(true);
        main.start(true);
    }
}
