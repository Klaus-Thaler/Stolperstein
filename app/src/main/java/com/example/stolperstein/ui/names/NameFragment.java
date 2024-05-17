package com.example.stolperstein.ui.names;

import static com.example.stolperstein.MainActivity.CacheFileName;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.FileManager;
import com.example.stolperstein.classes.utils;
import com.example.stolperstein.databinding.FragmentNameBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.osmdroid.bonuspack.kml.KmlDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NameFragment extends Fragment {

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_name, container, false);

        com.example.stolperstein.databinding.FragmentNameBinding binding = FragmentNameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //NameViewModel nameViewModel = new ViewModelProvider(this).get(NameViewModel.class);

        //mProgressBar = root.findViewById(R.id.name_progressBar);

        File cachefile = FileManager.loadCacheFile(getContext(), CacheFileName);
        KmlDocument kmlDoc = new KmlDocument();

        HashMap<Integer, List<String>> hashPerson = new HashMap<>();

        if (kmlDoc.parseKMLFile(cachefile)) {
            //utils.showToast(getContext(), "start");
            String kmlFile = FileManager.readCacheFile(getContext(), CacheFileName);
            Log.i("ST_NameFragment", "cachfile: " + kmlFile);
            Document doc = Jsoup.parse(kmlFile);
            Elements data = doc.select("Placemark");

            // todo nur 10 anzeigen und dann vor oder besser -> abc
            // for (int z = 0; 10 > z; z++) {
            for (int z = 0; data.size() > z; z++) {
                List<String> dataPerson = new ArrayList<>();
                Log.i("ST_NameFragment", "data: " + data);
                dataPerson.add(data.get(z).getElementsByTag("name").text());
                dataPerson.add(data.get(z).getElementsByTag("address").text());
                dataPerson.add(data.get(z).getElementsByTag("born").text());
                dataPerson.add(data.get(z).getElementsByTag("death").text());
                dataPerson.add(data.get(z).getElementsByTag("biographie").text());
                dataPerson.add(data.get(z).getElementsByTag("photo").text());
                dataPerson.add(data.get(z).getElementsByTag("installed").text());
                dataPerson.add(data.get(z).getElementsByTag("coordinates").text());
                hashPerson.put(z,dataPerson);
                Log.i("ST_NameFragment", ": " + z + " -" + dataPerson.get(0));
            }
        } else {
            utils.showToast(requireContext(), "no Data");
        }
        // Add the following lines to create RecyclerView
        // Add RecyclerView member
        RecyclerView recyclerView = root.findViewById(R.id.name_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        NameListAdapter nameListAdapter = new NameListAdapter(hashPerson, getContext());
        recyclerView.setAdapter(nameListAdapter);

        //utils.showToast(getContext(), "end");
        return root;
    }
}