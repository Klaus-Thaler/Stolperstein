package com.example.stolperstein.ui.names;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stolperstein.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView nameText;
    TextView addressText;
    TextView bornText;
    TextView deathText;
    TextView bioText;
    TextView fotoText;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        nameText = itemView.findViewById(R.id.name_name);
        addressText = itemView.findViewById(R.id.name_address);
        bornText = itemView.findViewById(R.id.name_born);
        deathText = itemView.findViewById(R.id.name_death);
        bioText = itemView.findViewById(R.id.name_bio);
        fotoText = itemView.findViewById(R.id.name_foto);
    }

    public TextView getView(){
        //view.setMovementMethod(LinkMovementMethod.getInstance());
        return nameText;
    }
}
