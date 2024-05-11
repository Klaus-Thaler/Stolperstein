package com.example.stolperstein.ui;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stolperstein.R;

import com.example.stolperstein.classes.utils;
import com.example.stolperstein.databinding.FragmentStonesBinding;

import java.io.IOException;

public class StonesFragment extends Fragment {
    public FragmentStonesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStonesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView title = binding.stonesTitle;
        title.setText(R.string.die_stolpersteine);
        title.setTextSize(24);

        String resString1 = getString(R.string.bei_den_steinen_handelt_es_sich);
        final TextView content1 = binding.stonesContent1;
        content1.setMovementMethod(LinkMovementMethod.getInstance());
        content1.setText(resString1);

        String resString2 = "Quelle: https://kiel-wiki.de/Stolpersteine";
        final TextView content2 = binding.stonesContent2;
        content2.setMovementMethod(LinkMovementMethod.getInstance());
        content2.setText(resString2);

        ImageView imageView = binding.stonesPic1;
        imageView.setCropToPadding(true);
        imageView.setMaxWidth(10);
        imageView.setMaxHeight(10);
        Bitmap img1 = utils.getBitmapFromAsset(requireContext(), "stolperstein_ein_mensch.png");
        imageView.setImageBitmap(img1);

        return root;
    }
}