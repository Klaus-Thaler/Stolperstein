package com.example.stolperstein.ui.names;

import static com.example.stolperstein.MainActivity.CacheFileName;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.webkit.WebViewFeature;

import com.example.stolperstein.classes.FileManager;
import com.example.stolperstein.R;
import com.example.stolperstein.databinding.FragmentNameBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.osmdroid.bonuspack.kml.KmlDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NameFragment extends Fragment {

    private FragmentNameBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_name, container, false);

        binding = FragmentNameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // darkmode (Let make me it simple)
        String css = "<style>body {background-color:white;color:black;} a {color:blue;}</style>";
        if (root.getResources().getString(R.string.mode).equals("night")) {
            css = "<style>body {background-color:black;color:white;} a {color:orange;}</style>";
        }


        NameViewModel nameViewModel =
                new ViewModelProvider(this).get(NameViewModel.class);
        File data = FileManager.loadCacheFile(getContext(), CacheFileName);
        KmlDocument kmlDoc = new KmlDocument();
        List<String> dataPerson = new ArrayList<>();
        if (kmlDoc.parseKMLFile(data)) {
            String kmlFile = FileManager.readCacheFile(getContext(), CacheFileName);
            Log.i("cachefile", ": " + kmlFile);
            Document doc = Jsoup.parse(kmlFile);
            Elements PMs = doc.select("data");
            for (int z = 0; PMs.size() > z; z++) {
                dataPerson.add("<html>"
                        + css + "<body>\n<table><tr><th colspan='2'>"
                        + PMs.get(z).getElementsByTag("name").text()
                        + "</th></tr>\n<tr><td>Adresse: </td><td>"
                        + PMs.get(z).getElementsByTag("address").text()
                        + "</td></tr>\n<tr><td>geboren: </td><td>"
                        + PMs.get(z).getElementsByTag("born").text()
                        + "</td></tr>\n<tr><td>deportiert/ermordet: </td><td>"
                        + PMs.get(z).getElementsByTag("death").text()
                        + "</td></tr>\n<tr><td>Biographie (PDF): </td><td>"
                        + PMs.get(z).getElementsByTag("biographie").html()
                        + "</td></tr>\n<tr><td>Foto (Link): </td><td>"
                        + PMs.get(z).getElementsByTag("photo").html()
                        + "</td></tr>\n</table></body></html>");
                Log.i("NameList", "-> " + dataPerson.get(z));
            }
        } else {
            String noData = getResources().getString(R.string.noData);
            dataPerson.add(noData);
        }
        // Add the following lines to create RecyclerView
        // Add RecyclerView member
        RecyclerView recyclerView = root.findViewById(R.id.name_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        NameListAdapter nameListAdapter = new NameListAdapter(dataPerson);
        recyclerView.setAdapter(nameListAdapter);
        return root;
    }
}