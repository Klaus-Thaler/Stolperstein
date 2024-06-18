package com.example.stolperstein.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.utils;
import com.example.stolperstein.databinding.FragmentStonesBinding;

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

        String resString1 = getString(R.string.this_stone1);
        final TextView content1 = binding.stonesContent1;
        content1.setMovementMethod(LinkMovementMethod.getInstance());
        content1.setText(resString1);

        String resString2 = getString(R.string.this_stone2);
        final TextView content2 = binding.stonesContent2;
        content2.setMovementMethod(LinkMovementMethod.getInstance());
        content2.setText(resString2);

        String resString3 = getString(R.string.this_stone3);
        final TextView content3 = binding.stonesContent3;
        content3.setMovementMethod(LinkMovementMethod.getInstance());
        content3.setText(resString3);

        String resString4 = getString(R.string.this_stone4);
        final TextView content4 = binding.stonesContent4;
        content4.setMovementMethod(LinkMovementMethod.getInstance());
        content4.setText(resString4);


        String resString5 = getString(R.string.quelle_wikipedia);
        final TextView content5 = binding.stonesContent5;
        content5.setMovementMethod(LinkMovementMethod.getInstance());
        content5.setText(resString5);

        ImageView imageView = binding.stonesPic1;
        imageView.setCropToPadding(true);
        imageView.setMaxWidth(10);
        imageView.setMaxHeight(10);
        Bitmap img1 = utils.getBitmapFromAsset(requireContext(), "stolperstein_ein_mensch.png");
        imageView.setImageBitmap(img1);

        return root;
    }
}