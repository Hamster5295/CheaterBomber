package com.hamster5295.qq_harker_bomber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.hamster5295.qq_harker_bomber.Bomber.Bomber;
import com.hamster5295.qq_harker_bomber.Bomber.BomberData;
import com.hamster5295.qq_harker_bomber.RecyclerView.SelectAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LaunchActivity extends AppCompatActivity {

    private RecyclerView mSelect;
    private StaggeredGridLayoutManager mManager;
    private SelectAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

//        testList();

        mSelect = findViewById(R.id.rv_select);
        adapter = new SelectAdapter(this, BomberData.getBombers());
        mManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mSelect.setAdapter(adapter);
        mSelect.setLayoutManager(mManager);

        readFile();

        adapter.refresh(BomberData.getBombers());

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                saveFile();
            }
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenubar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_add:
                BomberData.putBomber(new Bomber());
                adapter.refresh(BomberData.getBombers());
                Intent intent = new Intent(LaunchActivity.this, EditActivity.class);
                intent.putExtra("Index", adapter.getItemCount()-1);
                startActivityForResult(intent, 200);
                break;

            case R.id.btn_delete:
                startActivity(new Intent(LaunchActivity.this, SettingsActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        adapter.refresh(BomberData.getBombers());

        if (resultCode >= 0) {
            adapter.notifyItemRangeRemoved(resultCode, 1);
        }
    }

    @Override
    public void onBackPressed() {
        saveFile();
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

            case KeyEvent.KEYCODE_HOME:
                saveFile();
                super.onKeyDown(keyCode, event);
                return true;

            default:
                Log.i("a", "onKeyDown: " + keyCode);
                return super.onKeyDown(keyCode, event);
        }
    }

    private void saveFile() {

        try {
            ObjectOutputStream out = new ObjectOutputStream(openFileOutput("Bombers.json", MODE_PRIVATE));

            File outFile = new File(getFilesDir(), "Bombers.json");
            if (!outFile.exists())
                outFile.mkdirs();

            out.writeObject(BomberData.getBombers());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readFile() {
        try {
            ObjectInputStream in = new ObjectInputStream(openFileInput("Bombers.json"));
            BomberData.setBombers((ArrayList<Bomber>) in.readObject());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        saveFile();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        saveFile();
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        readFile();
        super.onRestoreInstanceState(savedInstanceState);
    }
}
