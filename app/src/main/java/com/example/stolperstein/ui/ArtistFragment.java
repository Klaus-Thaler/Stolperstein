package com.example.stolperstein.ui;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
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
import com.example.stolperstein.databinding.FragmentArtistBinding;
import com.example.stolperstein.classes.utils;

import java.io.IOException;
import java.io.InputStream;

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
        title.setTextSize(24);

        String resString1 = getString(R.string.gunter_demnig_27_oktober_1947_);
        final TextView content1 = binding.artistContent1;
        //content1.setMovementMethod(LinkMovementMethod.getInstance());
        content1.setText(resString1);

        String resString2 = getString(R.string.ausbildung);
        final TextView content2 = binding.artistContent2;
        content2.setText(resString2);
        title.setTextSize(22);

        String resString3 = getString(R.string.demnig_wuchs_in_nauen_und_berlin_);
        final TextView content3 = binding.artistContent3;
        //content3.setMovementMethod(LinkMovementMethod.getInstance());
        content3.setText(resString3);

        String resString4 = getString(R.string.beruf_und_werk);
        final TextView content4 = binding.artistContent4;
        content4.setText(resString4);
        title.setTextSize(22);

        String resString5 = getString(R.string._1985_er_ffnete_demnig_ein_eigenes_Atelier_);
        final TextView content5 = binding.artistContent5;
        //content5.setMovementMethod(LinkMovementMethod.getInstance());
        content5.setText(resString5);

        String resString6 = getString(R.string.bekannt_wurde_demnig_durch_die_Herstellung_);
        final TextView content6 = binding.artistContent6;
        //content6.setMovementMethod(LinkMovementMethod.getInstance());
        content6.setText(resString6);

        String resString7 = "Quelle: https://de.wikipedia.org/wiki/Gunter_Demnig";
        final TextView content7 = binding.artistContent7;
        content7.setText(resString7);
        content7.setMovementMethod(LinkMovementMethod.getInstance());

        ImageView imageView = binding.artistPic1;
        imageView.setCropToPadding(true);
        imageView.setMaxWidth(10);
        imageView.setMaxHeight(10);
        Bitmap img1 = utils.getBitmapFromAsset(requireContext(), "Stolpersteinverlegung_in_Pitten_(07).jpg");
        imageView.setImageBitmap(img1);

        return root;
    }
    private Bitmap getBitmapFromAsset(String fileName) throws IOException{
        AssetManager assetManager = getResources().getAssets();
        InputStream inStream = assetManager.open(fileName);
        return BitmapFactory.decodeStream(inStream);
    }
}