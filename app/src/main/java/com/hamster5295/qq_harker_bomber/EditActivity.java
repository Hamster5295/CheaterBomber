package com.hamster5295.qq_harker_bomber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hamster5295.qq_harker_bomber.Bomber.Bomber;
import com.hamster5295.qq_harker_bomber.Bomber.BomberData;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    private Bomber bomber;
    private int index;

    private EditText edit_url;
    private EditText edit_user;
    private EditText edit_pass;
    private EditText edit_extra;
    private EditText edit_prefix;
    private EditText edit_description;

    private LinearLayout layout_extra;
    private LinearLayout layout_prefix;

    private Switch switch_base64;
    private Switch switch_extra;
    private Switch switch_prefix;
    private Switch switch_get;

    @SuppressLint("HandlerLeak")
    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            layout_extra.setVisibility(bomber.getUseExtra() ? View.VISIBLE : View.GONE);
            layout_prefix.setVisibility(bomber.getUsePrefix() ? View.VISIBLE : View.GONE);
        }
    };

    private boolean thread_checkUsage_flag = true;
    private Thread thread_checkUsage = new Thread(() -> {
        while (thread_checkUsage_flag) {
            uiHandler.sendEmptyMessage(0);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setResult(-1);

        if (getIntent() != null) {
            index = getIntent().getIntExtra("Index", 0);
        }
        bomber = BomberData.getBomber(index);

        if (bomber == null) return;

        edit_url = findViewById(R.id.editText_url);
        edit_user = findViewById(R.id.editText_user);
        edit_pass = findViewById(R.id.editText_pass);
        edit_extra = findViewById(R.id.editText_extra);
        edit_prefix = findViewById(R.id.editText_prefix);
        edit_description = findViewById(R.id.editText_description);

        layout_extra = findViewById(R.id.layout_extra);
        layout_prefix = findViewById(R.id.layout_prefix);

        switch_base64 = findViewById(R.id.switch_base64);
        switch_extra = findViewById(R.id.switch_extra);
        switch_prefix = findViewById(R.id.switch_prefix);
        switch_get = findViewById(R.id.switch_get);

        edit_url.setText(bomber.getUrl());
        edit_user.setText(bomber.getUserKey());
        edit_pass.setText(bomber.getPasswordKey());
        edit_extra.setText(bomber.getExtra());
        edit_prefix.setText(bomber.getPrefix());
        edit_description.setText(bomber.getDescription());

        switch_base64.setChecked(bomber.getUseBase64());
        switch_extra.setChecked(bomber.getUseExtra());
        switch_prefix.setChecked(bomber.getUsePrefix());

        thread_checkUsage_flag = true;
        thread_checkUsage.start();

        switch_base64.setOnClickListener(view -> {

        });

        switch_extra.setOnClickListener(view -> {
            bomber.setUseExtra(switch_extra.isChecked());
        });

        switch_prefix.setOnClickListener(view -> {
            bomber.setUsePrefix(switch_prefix.isChecked());
        });

        switch_get.setOnClickListener(view -> {
            bomber.setUseGet(switch_get.isChecked());
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //TODO: Save edit.
                saveToBomber();
                thread_checkUsage_flag = false;
                finish();
                return true;

            case R.id.btn_delete:
                BomberData.removeBomber(index);
                setResult(index);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveToBomber() {
        bomber.setUrl(edit_url.getText().toString());
        bomber.setUserKey(edit_user.getText().toString());
        bomber.setPasswordKey(edit_pass.getText().toString());
        bomber.setExtra(edit_extra.getText().toString());
        bomber.setPrefix(edit_prefix.getText().toString());
        bomber.setDescription(edit_description.getText().toString());
        bomber.setUseBase64(switch_base64.isChecked());
        bomber.setUseExtra(switch_extra.isChecked());
        bomber.setUsePrefix(switch_prefix.isChecked());
        bomber.setUseGet(switch_get.isChecked());
//        bomber.setUseProxy();

        BomberData.setBomber(index, bomber);
//        Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        saveToBomber();
        thread_checkUsage_flag = false;
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editmenubar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
