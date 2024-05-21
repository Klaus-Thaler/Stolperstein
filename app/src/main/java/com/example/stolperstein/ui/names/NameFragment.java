package com.example.stolperstein.ui.names;

import static com.example.stolperstein.MainActivity.CacheFileName;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ListenableWorker;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.FileManager;
import com.example.stolperstein.classes.utils;
import com.example.stolperstein.databinding.FragmentNameBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NameFragment extends Fragment {
    public ProgressBar progressBar;
    private final HashMap<Integer, List<String>> hashPerson = new HashMap<>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_name, container, false);

        FragmentNameBinding binding = FragmentNameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //NameViewModel nameViewModel = new ViewModelProvider(this).get(NameViewModel.class);

        if (FileManager.CacheFileExist(requireContext(), CacheFileName)) {
            KmlDocument kmlDoc = new KmlDocument();
            File kmlFile = FileManager.loadCacheFile(getContext(), CacheFileName);
            try {
                kmlDoc.parseKMLFile(kmlFile);
                Document doc = Jsoup.parse(kmlFile);
                Elements data = doc.select("Placemark");
                // todo nur 10 anzeigen und dann vor oder besser -> abc
                //for (int z = 0; 1 > z; z++) {
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
            } catch (IOException e) {
                Log.i("ST_getNameWorker", "result: " + ListenableWorker.Result.failure());
                throw new RuntimeException(e);
            } finally {
                Log.i("ST_getNameWorker", "finally result: " + ListenableWorker.Result.success());
            }
        } else {
            utils.showToast(requireContext(), "no Data");
        }
        // Add the following lines to create RecyclerView
        // Add RecyclerView member

        RecyclerView recyclerView = root.findViewById(R.id.name_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        NameListAdapter nameListAdapter = new NameListAdapter(hashPerson, root.getContext());
        recyclerView.setAdapter(nameListAdapter);

        //utils.showToast(getContext(), "end");
        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(requireActivity(),
                PreferenceManager.getDefaultSharedPreferences(requireActivity()));
    }
    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().load(requireActivity(),
                PreferenceManager.getDefaultSharedPreferences(requireActivity()));
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
}