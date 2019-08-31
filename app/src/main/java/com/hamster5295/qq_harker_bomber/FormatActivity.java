package com.hamster5295.qq_harker_bomber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hamster5295.qq_harker_bomber.Bomber.Bomber;
import com.hamster5295.qq_harker_bomber.Bomber.BomberData;
import com.hamster5295.qq_harker_bomber.RecyclerView.FormatAdapter;

import java.util.ArrayList;

public class FormatActivity extends AppCompatActivity {

    private int index;
    private Bomber bomber;

    private FormatAdapter adapter;

    private RecyclerView recycler_format;
    private Button btn_addText;
    private Button btn_addRN;
    private Button btn_addRP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_format);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycler_format = findViewById(R.id.recycler_format);
        btn_addText = findViewById(R.id.btn_addText);
        btn_addRN = findViewById(R.id.btn_addRN);
        btn_addRP = findViewById(R.id.btn_addRP);

        Intent i = getIntent();
        if (i != null) {
            index = i.getIntExtra("Index", 0);
        }

        bomber = BomberData.getBomber(index);

        adapter = new FormatAdapter(this, bomber.getFormat());
        recycler_format.setAdapter(adapter);
        recycler_format.setLayoutManager(new LinearLayoutManager(this));

        for (String item : bomber.getFormat()) {
            adapter.addFormat(item);
        }

        btn_addText.setOnClickListener(view -> {
            adapter.addFormat("");
        });

        btn_addRN.setOnClickListener(view -> {
            adapter.addFormat("%Random_Number%");
        });

        btn_addRP.setOnClickListener(view -> {
            adapter.addFormat("%Random_Password%");
        });
    }

    @Override
    protected void onDestroy() {
        bomber.setFormat(adapter.getFormat());
        BomberData.setBomber(index, bomber);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //TODO: Save edit.
                bomber.setFormat(adapter.getFormat());
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        bomber.setFormat(adapter.getFormat());
        super.onBackPressed();
    }
}
