package com.example.stolperstein.ui.names;


import static com.example.stolperstein.MainActivity.hashPerson;
import static com.example.stolperstein.classes.sqlHandler.getInstance;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stolperstein.R;
import com.example.stolperstein.classes.sqlHandler;
import com.example.stolperstein.classes.utils;
import com.example.stolperstein.databinding.FragmentNameBinding;

public class NameFragment extends Fragment {
    ArrayAdapter<String> arrayAdapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_name, container, false);

        FragmentNameBinding binding = FragmentNameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        NameViewModel nameViewModel = new ViewModelProvider(this).get(NameViewModel.class);

        // creating a new db handler class
        sqlHandler sqlHandler = getInstance(root.getContext());
        // sql daten holen
        String query = "SELECT * FROM person;";

        hashPerson = sqlHandler.getHashMapFromData(query);
        Log.i("ST_NameFragment", "query: "+ hashPerson.toString());


        utils.showToast(getContext(), "count " + hashPerson.size());
        //Log.i("ST_NameFragment", "- " + hashPerson.toString());

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
        searchView.setQueryHint("Suche nach Namen ...");

        arrayAdapter = new ArrayAdapter<>(root.getContext(), R.layout.searchtextview, sqlHandler.getNames());

        // autofill
        // todo  List<String> fillList = sqlHandler.getNames();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchText) {
                String query ="SELECT * FROM person WHERE person.name LIKE '" + searchText + "%'";
                //Log.i("ST_NameFragment", "query: "+ sqlHandler.getHashMapFromData(query));
                hashPerson = sqlHandler.getHashMapFromData(query);
                utils.showToast(root.getContext(), hashPerson.size() + " Entry(s) found");
                // new adapter, new recycler
                NameListAdapter nameListAdapter = new NameListAdapter(hashPerson, root.getContext());
                recyclerView.setAdapter(nameListAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //utils.showToast(root.getContext(), "change: " + newText);
                arrayAdapter.getFilter().filter(newText);
                // todo
                return false;
            }
        });
        //Log.i("ST_NameFragment", "- " + sqlHandler.getNames().toString());

        //List<String> arrayList = new ArrayList<>(sqlHandler.getNames());
        //ArrayAdapter<String> adapter  = new ArrayAdapter<>(root.getContext(), R.layout.searchtextview, arrayList);
        //SearchView searchView = root.findViewById(R.id.search);

        //searchView.setOnCloseListener();

        //CharSequence query = SearchView.getQueryHint(); // get the query string currently in the text field

        //Log.i("ST_NameFragment", "- " + query);

        //utils.showToast(root.getContext(), "-" + query);

        //searchView.setAdapter(namens);


        //searchText.setAda

        //searchView.setOnClickListener(new );

        return root;
    }
    /*
    public boolean onQueryTextSubmit(String query) {
        //final List<Data> filteredModelList = filter(dabListItem, query);
        //NameListAdapter.setItems(filteredModelList);
        NameListAdapter.notifyDataSetChanged();
        //NameListAdapter.scrollToPosition(0);

        return false;
    }

     */

    /*
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(requireActivity(),
                PreferenceManager.getDefaultSharedPreferences(requireActivity()));
        NameFragment.super.onResume();
        //if (mapView != null) { mapView.onResume(); }
    }
    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().load(requireActivity(),
                PreferenceManager.getDefaultSharedPreferences(requireActivity()));
        NameFragment.super.onPause();
        //if (mapView != null) { mapView.onPause(); }
    }

     */

}