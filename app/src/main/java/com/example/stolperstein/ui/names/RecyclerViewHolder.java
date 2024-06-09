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
        nameText.setTextSize(mSharedPref.getFloat("mFontSize_20.0",20F));
        addressText = itemView.findViewById(R.id.name_address);
        addressText.setTextSize(mSharedPref.getFloat("mFontSize_14.0",14F));
        bornText = itemView.findViewById(R.id.name_born);
        bornText.setTextSize(mSharedPref.getFloat("mFontSize_14.0",14F));
        deathText = itemView.findViewById(R.id.name_death);
        deathText.setTextSize(mSharedPref.getFloat("mFontSize_14.0",14F));
        installedText = itemView.findViewById(R.id.name_installed);
        installedText.setTextSize(mSharedPref.getFloat("mFontSize_14.0",14F));
        bioButton = itemView.findViewById(R.id.name_button_bio);
        fotoButton = itemView.findViewById(R.id.name_button_foto);
        geopointButton = itemView.findViewById(R.id.name_button_geopoint);
        counter = itemView.findViewById(R.id.name_count);
        counter.setTextSize(mSharedPref.getFloat("mFontSize_14.0",14F));
    }
}
