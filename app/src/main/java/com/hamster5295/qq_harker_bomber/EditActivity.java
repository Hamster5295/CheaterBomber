package com.hamster5295.qq_harker_bomber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.hamster5295.qq_harker_bomber.Bomber.Bomber;
import com.hamster5295.qq_harker_bomber.Bomber.BomberData;

import java.io.Serializable;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    private Bomber bomber;
    private int index;

    private EditText edit_url;
    private EditText edit_description;
    private Button btn_format;

    private Switch switch_base64;
    private Switch switch_get;


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
        edit_description = findViewById(R.id.editText_description);
        btn_format = findViewById(R.id.btn_format);

        switch_base64 = findViewById(R.id.switch_base64);
        switch_get = findViewById(R.id.switch_get);

        edit_url.setText(bomber.getUrl());
        edit_description.setText(bomber.getDescription());

        switch_base64.setChecked(bomber.getUseBase64());
        switch_base64.setOnClickListener(view -> {
            bomber.setUseBase64(switch_base64.isChecked());
        });

        switch_get.setOnClickListener(view -> {
            bomber.setUseGet(switch_get.isChecked());
        });

        btn_format.setOnClickListener(view -> {
            Intent i = new Intent(EditActivity.this, FormatActivity.class);
            i.putExtra("Index", index);
            startActivityForResult(i, 1000);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //TODO: Save edit.
                saveToBomber();
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
        bomber.setDescription(edit_description.getText().toString());
        bomber.setUseBase64(switch_base64.isChecked());
        bomber.setUseGet(switch_get.isChecked());
//        bomber.setUseProxy();

        BomberData.setBomber(index, bomber);
//        Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        saveToBomber();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editmenubar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bomber = BomberData.getBomber(index);
    }
}
