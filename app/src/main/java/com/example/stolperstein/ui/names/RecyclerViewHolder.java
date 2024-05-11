package com.example.stolperstein.ui.names;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stolperstein.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    //private final TextView view;
    private final WebView webView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        webView = itemView.findViewById(R.id.name_webview);
    }

    public WebView getView(){
        //view.setMovementMethod(LinkMovementMethod.getInstance());
        return webView;
    }
}
