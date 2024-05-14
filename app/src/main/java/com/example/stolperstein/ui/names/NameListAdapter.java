package com.example.stolperstein.ui.names;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stolperstein.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NameListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private final HashMap<Integer,List<String>> data;

    public NameListAdapter(HashMap<Integer, List<String>> data) {
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
        List<String> show = Objects.requireNonNull(data.get(position));
        holder.nameText.setText(show.get(0));
        holder.addressText.setText(show.get(1));
        holder.bornText.setText(show.get(2));
        holder.deathText.setText(show.get(3));
        holder.bioText.setText(show.get(4));
        holder.fotoText.setText(show.get(5));
        Log.i("NameListAdapter", "-> " + show.get(0));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}