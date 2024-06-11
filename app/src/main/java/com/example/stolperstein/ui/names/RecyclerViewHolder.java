package com.example.stolperstein.ui.names;

import static com.example.stolperstein.MainActivity.mSharedPref;

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
        nameText.setTextSize(mSharedPref.getInt("mFontSize_20",20));
        addressText = itemView.findViewById(R.id.name_address);
        addressText.setTextSize(mSharedPref.getInt("mFontSize_14",14));
        bornText = itemView.findViewById(R.id.name_born);
        bornText.setTextSize(mSharedPref.getInt("mFontSize_14",14));
        deathText = itemView.findViewById(R.id.name_death);
        deathText.setTextSize(mSharedPref.getInt("mFontSize_14",14));
        installedText = itemView.findViewById(R.id.name_installed);
        installedText.setTextSize(mSharedPref.getInt("mFontSize_14",14));
        bioButton = itemView.findViewById(R.id.name_button_bio);
        fotoButton = itemView.findViewById(R.id.name_button_foto);
        geopointButton = itemView.findViewById(R.id.name_button_geopoint);
        counter = itemView.findViewById(R.id.name_count);
        counter.setTextSize(mSharedPref.getInt("mFontSize_14",14));
    }
}
