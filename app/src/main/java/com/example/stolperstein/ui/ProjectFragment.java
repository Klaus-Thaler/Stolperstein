package com.example.stolperstein.ui;

import static com.example.stolperstein.MainActivity.mSharedPref;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.stolperstein.databinding.FragmentProjectBinding;

import java.io.IOException;
import java.io.InputStream;

public class ProjectFragment extends Fragment {
    public FragmentProjectBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View rootView = inflater.inflate(R.layout.fragment_project, container, false);

        binding = FragmentProjectBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView title = binding.projectTitle;
        title.setText(R.string.das_kunstprojekt);
        title.setTextSize(mSharedPref.getInt("mFontSize_24",24));

        String resString1 = getResources().getString(R.string.this_project1);
        final TextView content1 = binding.projectContent1;
        content1.setTextSize(mSharedPref.getInt("mFontSize_16",16));
        content1.setMovementMethod(LinkMovementMethod.getInstance());
        content1.setText(resString1);

        String resString2 = getResources().getString(R.string.this_project2);
        final TextView content2 = binding.projectContent2;
        content2.setTextSize(mSharedPref.getInt("mFontSize_16",16));
        content2.setMovementMethod(LinkMovementMethod.getInstance());
        content2.setText(resString2);

        String resString3 = getResources().getString(R.string.this_project3);
        final TextView content3 = binding.projectContent3;
        content3.setTextSize(mSharedPref.getInt("mFontSize_16",16));
        content3.setMovementMethod(LinkMovementMethod.getInstance());
        content3.setText(resString3);

        String resString4 = getResources().getString(R.string.this_project4);
        final TextView content4 = binding.projectContent4;
        content4.setTextSize(mSharedPref.getInt("mFontSize_16",16));
        content4.setMovementMethod(LinkMovementMethod.getInstance());
        content4.setText(resString4);

        String resString5 = "Quelle: https://kiel-wiki.de/Stolpersteine";
        final TextView content5 = binding.projectContent5;
        content5.setTextSize(mSharedPref.getInt("mFontSize_16",16));
        content5.setMovementMethod(LinkMovementMethod.getInstance());
        content5.setText(resString5);

        ImageView imageView = binding.projectPic1;
        imageView.setCropToPadding(true);
        imageView.setMaxWidth(10);
        imageView.setMaxHeight(10);
        Bitmap img1 = utils.getBitmapFromAsset(requireContext(), "800px-Gunter_Demnig_in_Kiel,_2013.JPG");
        imageView.setImageBitmap(img1);

        return root;
    }
}