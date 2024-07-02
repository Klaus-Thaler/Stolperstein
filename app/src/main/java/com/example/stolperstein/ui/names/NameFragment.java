package com.example.stolperstein.ui.names;


import static com.example.stolperstein.MainActivity.hashPerson;
import static com.example.stolperstein.classes.sqlHandler.getInstance;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.sqlHandler;
import com.example.stolperstein.databinding.FragmentNameBinding;

public class NameFragment extends Fragment {

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_name, container, false);

        FragmentNameBinding binding = FragmentNameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //NameViewModel nameViewModel = new ViewModelProvider(this).get(NameViewModel.class);

        // creating a new db handler class
        sqlHandler sqlHandler = getInstance(root.getContext());
        // sql daten holen
        String query = "SELECT * FROM person;";

        hashPerson = sqlHandler.getHashMapFromData(query);

        // Add the following lines to create RecyclerView
        // Add RecyclerView member
        RecyclerView recyclerView = root.findViewById(R.id.name_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        // set adapter
        NameListAdapter nameListAdapter = new NameListAdapter(hashPerson, root.getContext());
        recyclerView.setAdapter(nameListAdapter);

        // search (die Lupe)
        SearchView searchView = root.findViewById(R.id.searchtext);
        searchView.setFocusable(true);
        searchView.setQueryHint("Search ...");

        TextView searchResult = root.findViewById(R.id.searchResult);
        searchResult.setText(hashPerson.size() + " Entry(s) found");

        //arrayAdapter = new ArrayAdapter<>(root.getContext(), R.layout.searchtextview, sqlHandler.getAllNames());

        // autofill
        // todo  List<String> fillList = sqlHandler.getNames();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchText) {
                String query ="SELECT * FROM person WHERE person.name LIKE '" + searchText + "%'";
                hashPerson = sqlHandler.getHashMapFromData(query);
                //utils.showToast(root.getContext(), hashPerson.size() + " Entry(s) found");
                // new adapter, new recycler
                NameListAdapter nameListAdapter = new NameListAdapter(hashPerson, root.getContext());
                recyclerView.setAdapter(nameListAdapter);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                String query ="SELECT * FROM person WHERE person.name LIKE '" + searchText + "%'";
                hashPerson = sqlHandler.getHashMapFromData(query);
                //utils.showToast(root.getContext(), hashPerson.size() + " Entry(s) found");
                searchResult.setText(hashPerson.size() + " Entry(s) found");
                // new adapter, new recycler
                NameListAdapter nameListAdapter = new NameListAdapter(hashPerson, root.getContext());
                recyclerView.setAdapter(nameListAdapter);

                return false;
            }
        });
        return root;
    }

}