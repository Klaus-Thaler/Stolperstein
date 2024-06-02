package com.example.stolperstein.ui.names;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stolperstein.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView nameText;
    TextView addressText;
    TextView bornText;
    TextView deathText;
    TextView installedText;
    Button bioButton;
    Button fotoButton;
    Button geopointButton;
    TextView counter;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        nameText = itemView.findViewById(R.id.name_name);
        addressText = itemView.findViewById(R.id.name_address);
        bornText = itemView.findViewById(R.id.name_born);
        deathText = itemView.findViewById(R.id.name_death);
        installedText = itemView.findViewById(R.id.name_installed);
        bioButton = itemView.findViewById(R.id.name_button_bio);
        fotoButton = itemView.findViewById(R.id.name_button_foto);
        geopointButton = itemView.findViewById(R.id.name_button_geopoint);
        counter = itemView.findViewById(R.id.name_count);
    }
}
