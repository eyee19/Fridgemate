package com.example.eyee3.fridgemate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class FridgeFragment extends Fragment {

    public FridgeFragment() {

    }

    private ListView mFridgeList;
    private Button mAddButton;
    private ArrayAdapter<String> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Your Fridge");
        View view = inflater.inflate(R.layout.fragment_fridge, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mFridgeList = (ListView) getView().findViewById(R.id.fridge_listView);
        mAddButton = (Button) getView().findViewById(R.id.add_button);

        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        mFridgeList.setAdapter(mAdapter);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Going to add activity", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
