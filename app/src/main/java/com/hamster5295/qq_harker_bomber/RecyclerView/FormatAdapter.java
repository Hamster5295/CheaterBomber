package com.hamster5295.qq_harker_bomber.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamster5295.qq_harker_bomber.Bomber.FormatType;
import com.hamster5295.qq_harker_bomber.R;

import java.util.ArrayList;

public class FormatAdapter extends RecyclerView.Adapter<FormatAdapter.ViewHolder> {

    public ArrayList<String> getFormat() {
        return format;
    }

    public void setFormat(ArrayList<String> format) {
        this.format = format;
    }

    private ArrayList<String> format;
    private Activity activity;

    public FormatAdapter(Activity activity, ArrayList<String> format) {
        this.activity = activity;
        this.format = new ArrayList<>();
        notifyItemRangeChanged(0, format.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_format, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        init(holder, position);
        holder.btn_edit.setOnClickListener(view -> {

            EditText editText = new EditText(activity);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(10, 0, 0, 10);
            editText.setLayoutParams(p);

            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle("请输入内容")
                    .setView(editText)
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        holder.setText(editText.getText().toString());
                        format.set(position, editText.getText().toString());
                    })
                    .create();
            dialog.show();
        });

        holder.getBtn_del().setOnClickListener(view -> {
            notifyItemRemoved(position);
            format.remove(position);
            notifyItemRangeChanged(position, format.size() - position);
        });
    }


    @Override
    public int getItemCount() {
        if (format != null) {
            return format.size();
        }

        return 0;
    }

    public void addFormat(String s) {
        format.add(s);
        notifyItemChanged(format.size() - 1);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
            if (!text.equals("%Random_Number%") && !text.equals("%Random_Password%"))
                tv_Text.setText(text);
        }

        public FormatType getType() {
            return type;
        }

        public void setType(FormatType type) {
            this.type = type;
        }

        public ImageButton getBtn_edit() {
            return btn_edit;
        }

        public void setBtn_edit(ImageButton btn_edit) {
            this.btn_edit = btn_edit;
        }

        public ImageButton getBtn_del() {
            return btn_del;
        }

        public void setBtn_del(ImageButton btn_del) {
            this.btn_del = btn_del;
        }

        public TextView getTv_Text() {
            return tv_Text;
        }

        public void setTv_Text(TextView tv_Text) {
            this.tv_Text = tv_Text;
        }

        private String text = "";
        private FormatType type = FormatType.TEXT;

        private ImageButton btn_edit;
        private ImageButton btn_del;
        private TextView tv_Text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_del = itemView.findViewById(R.id.btn_deleteFormat);
            tv_Text = itemView.findViewById(R.id.text_format);
        }
    }

    private void init(ViewHolder holder, int position) {

        switch (format.get(position)) {
            case "%Random_Number%":
                holder.getTv_Text().setTextColor(Color.BLUE);
                holder.getTv_Text().setText("[随机数字]");
                holder.setText("%Random_Number%");
                holder.getBtn_edit().setVisibility(View.GONE);
                break;

            case "%Random_Password%":
                holder.getTv_Text().setTextColor(Color.BLUE);
                holder.getTv_Text().setText("[随机密码]");
                holder.setText("%Random_Password%");
                holder.getBtn_edit().setVisibility(View.GONE);
                break;

            default:
                holder.getTv_Text().setTextColor(Color.BLACK);
                holder.getTv_Text().setText(format.get(position));
                break;
        }

    }
}
