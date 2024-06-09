package com.example.stolperstein.ui;

import static com.example.stolperstein.MainActivity.mSharedPref;

import android.annotation.SuppressLint;
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
import com.example.stolperstein.databinding.FragmentArtistBinding;

public class ArtistFragment extends Fragment {

    private FragmentArtistBinding binding;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArtistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Inflate the layout for this fragment
        final TextView title = binding.artistTitle;
        title.setText(R.string.gunter_demnig);
        title.setTextSize(mSharedPref.getInt("mFontSize_24",24));

        String resString1 = getString(R.string.this_artist1);
        final TextView content1 = binding.artistContent1;
        content1.setTextSize(mSharedPref.getInt("mFontSize_16",16));
        content1.setMovementMethod(LinkMovementMethod.getInstance());
        content1.setText(resString1);

        String resString2 = getString(R.string.this_artist2);
        final TextView content2 = binding.artistContent2;
        content2.setTextSize(mSharedPref.getInt("mFontSize_16",16));
        content2.setMovementMethod(LinkMovementMethod.getInstance());
        content2.setText(resString2);

        String resString3 = getString(R.string.this_artist3);
        final TextView content3 = binding.artistContent3;
        content3.setTextSize(mSharedPref.getInt("mFontSize_16",16));
        content3.setMovementMethod(LinkMovementMethod.getInstance());
        content3.setText(resString3);

        String resString4 = getString(R.string.this_artist4);
        final TextView content4 = binding.artistContent4;
        content4.setTextSize(mSharedPref.getInt("mFontSize_16",16));
        content4.setMovementMethod(LinkMovementMethod.getInstance());
        content4.setText(resString4);

        String resString5 = getString(R.string.source_github);
        final TextView content5 = binding.artistContent5;
        content5.setTextSize(mSharedPref.getInt("mFontSize_16",16));
        content5.setMovementMethod(LinkMovementMethod.getInstance());
        content5.setText(resString5);

        ImageView imageView = binding.artistPic1;
        imageView.setCropToPadding(true);
        imageView.setMaxWidth(10);
        imageView.setMaxHeight(10);
        Bitmap img1 = utils.getBitmapFromAsset(requireContext(), "Stolpersteinverlegung_in_Pitten_(07).jpg");
        imageView.setImageBitmap(img1);

        return root;
    }
}