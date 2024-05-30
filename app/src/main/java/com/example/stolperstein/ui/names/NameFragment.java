package com.example.stolperstein.ui.names;


import static com.example.stolperstein.MainActivity.hashPerson;
import static com.example.stolperstein.classes.sqlHandler.getInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_name, container, false);

        FragmentNameBinding binding = FragmentNameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        NameViewModel nameViewModel = new ViewModelProvider(this).get(NameViewModel.class);

        // creating a new db handler class
        sqlHandler sqlHandler = getInstance(getContext());
        // sql daten holen
        hashPerson = sqlHandler.getAllPersonData();
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

        return root;
    }
}