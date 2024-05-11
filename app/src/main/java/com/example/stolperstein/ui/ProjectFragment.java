package com.example.stolperstein.ui;

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
import androidx.lifecycle.ViewModelProvider;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.utils;
import com.example.stolperstein.databinding.FragmentArtistBinding;
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
        title.setTextSize(24);

        String resString1 = getResources().getString(R.string.this_project);
        final TextView content1 = binding.projectContent1;
        content1.setMovementMethod(LinkMovementMethod.getInstance());
        content1.setText(resString1);

        String resString2 = "Quelle: https://kiel-wiki.de/Stolpersteine";
        final TextView content2 = binding.projectContent2;
        content2.setMovementMethod(LinkMovementMethod.getInstance());
        content2.setText(resString2);


        ImageView imageView = binding.projectPic1;
        imageView.setCropToPadding(true);
        imageView.setMaxWidth(10);
        imageView.setMaxHeight(10);
        Bitmap img1 = utils.getBitmapFromAsset(requireContext(), "800px-Gunter_Demnig_in_Kiel,_2013.JPG");
        imageView.setImageBitmap(img1);

        return root;
    }

    private Bitmap getBitmapFromAsset(String s) throws IOException{
        AssetManager assetManager = getResources().getAssets();
        InputStream isstr = assetManager.open(s);
        return BitmapFactory.decodeStream(isstr);
    }
}