package com.example.stolperstein.ui.names;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Application;
import android.graphics.Bitmap;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NameListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private final List<String> data;

    public NameListAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.name_textview;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String show = Objects.requireNonNull(data.get(position));
        // webview
        holder.getView().loadData(show,"text/html", "UTF-8");
        Log.i("NameListAdapter", "-> " + show);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}