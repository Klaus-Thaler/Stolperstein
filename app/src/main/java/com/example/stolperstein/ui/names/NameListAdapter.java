package com.example.stolperstein.ui.names;

import static com.example.stolperstein.classes.sqlHandler.getInstance;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.sqlHandler;
import com.example.stolperstein.classes.utils;
import com.example.stolperstein.ui.DialogSingleMap;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NameListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private final HashMap<Integer,List<String>> data;
    private final Context mContext;
    public NameListAdapter(HashMap<Integer,List<String>> data, Context context) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.name_textview;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        List<String> show = Objects.requireNonNull(data.get(position));
        holder.nameText.setText(show.get(0));
        holder.nameText.setAutoSizeTextTypeUniformWithConfiguration(16, 22,1,1);
        holder.addressText.setText(show.get(1));
        holder.addressText.setAutoSizeTextTypeUniformWithConfiguration(8,16,1,1);
        holder.bornText.setText(show.get(2));
        holder.bornText.setAutoSizeTextTypeUniformWithConfiguration(8,16,1,1);
        holder.deathText.setText(show.get(3));
        holder.deathText.setAutoSizeTextTypeUniformWithConfiguration(8,16,1,1);
        holder.installedText.setText(show.get(6));
        holder.installedText.setAutoSizeTextTypeUniformWithConfiguration(8,16,1,1);
        holder.counter.setText(String.valueOf(position+1));
        holder.counter.setAutoSizeTextTypeUniformWithConfiguration(8,16,1,1);

        holder.bioButton.setAutoSizeTextTypeUniformWithConfiguration(8,16,1,1);
        holder.fotoButton.setAutoSizeTextTypeUniformWithConfiguration(8,16,1,1);
        holder.geopointButton.setAutoSizeTextTypeUniformWithConfiguration(8,16,1,1);

        // buttons
        holder.bioButton.setOnClickListener(v -> {
            //utils.showToast(v.getContext(), "klick pdf");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(show.get(4)));
            mContext.startActivity(intent);
        });
        holder.fotoButton.setOnClickListener(v -> {
            //utils.showToast(v.getContext(), "klick foto");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(show.get(5)));
            mContext.startActivity(intent);
        });
        holder.geopointButton.setOnClickListener(v -> {
            //utils.showToast(v.getContext(), "klick geopoint");

            // creating a new db handler class
            sqlHandler sqlHandler = getInstance(mContext);
            String geoPoint = sqlHandler.getGeoPoint(show.get(1));

            if (!Objects.equals(geoPoint, "")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    DialogSingleMap.show(v.getContext(), "title", show.get(0), show.get(1), geoPoint);
                }
            } else {
                utils.showToast(mContext, "no GeoPoint for this address. ;-(");
            }
        });
    }

    @Override
    public int getItemCount() { return data.size(); }
}