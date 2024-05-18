package com.example.stolperstein.ui.names;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stolperstein.R;
import com.example.stolperstein.ui.DialogMap;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NameListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private final HashMap<Integer,List<String>> data;
    private final Context mContext;
    public NameListAdapter(HashMap<Integer, List<String>> data, Context context) {
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
        holder.addressText.setText(show.get(1));
        holder.bornText.setText(show.get(2));
        holder.deathText.setText(show.get(3));
        holder.installedText.setText(show.get(6));

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                DialogMap.show(v.getContext(), "title", show.get(0), show.get(1), show.get(7));
            }
        });
        Log.i("ST_NameListAdapter", "-> " + show.get(0));
    }

    @Override
    public int getItemCount() { return data.size(); }
}