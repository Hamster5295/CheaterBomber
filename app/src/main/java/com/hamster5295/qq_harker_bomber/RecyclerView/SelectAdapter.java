package com.hamster5295.qq_harker_bomber.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamster5295.qq_harker_bomber.Bomber.Bomber;
import com.hamster5295.qq_harker_bomber.Bomber.BomberData;
import com.hamster5295.qq_harker_bomber.EditActivity;
import com.hamster5295.qq_harker_bomber.PerformActivity;
import com.hamster5295.qq_harker_bomber.R;

import java.util.ArrayList;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {

    private Activity mContext;
    private ArrayList<Bomber> mList;

    public SelectAdapter(Activity mContext, ArrayList<Bomber> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void addBomber(Bomber b) {
        mList.add(b);
        notifyItemChanged(mList.size() - 1);
    }

    public void refresh(ArrayList<Bomber> a) {
        mList = a;
        for (int i = 0; i < mList.size(); i++) {
            notifyItemChanged(i);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bomber, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder v, int position) {
        v.getText_url().setText(mList.get(position).getUrl());
        v.getText_description().setText(mList.get(position).getDescription());

        v.getBtn_start().setOnClickListener((view) -> {
            Intent intent = new Intent(mContext, PerformActivity.class);
            intent.putExtra("Index", position);
            mContext.startActivityForResult(intent, 100);
        });

        v.getBtn_edit().setOnClickListener(view -> {
            Intent intent = new Intent(mContext, EditActivity.class);
            intent.putExtra("Index", position);
            mContext.startActivityForResult(intent, 200);
        });
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text_url;
        private TextView text_description;
        private ImageButton btn_start;
        private ImageButton btn_edit;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_url = itemView.findViewById(R.id.text_url);
            text_description = itemView.findViewById(R.id.text_description);
            btn_start = itemView.findViewById(R.id.button_start);
            btn_edit = itemView.findViewById(R.id.button_edit);
        }

        public TextView getText_url() {
            return text_url;
        }

        public TextView getText_description() {
            return text_description;
        }

        public ImageButton getBtn_start() {
            return btn_start;
        }

        public ImageButton getBtn_edit() {
            return btn_edit;
        }
    }
}
