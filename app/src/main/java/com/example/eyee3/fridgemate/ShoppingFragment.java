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

import java.util.List;

public class ShoppingFragment extends Fragment {

    public ShoppingFragment() {

    }
    private ListView mShoppingList;
    private EditText mItemEdit;
    private Button mAddButton;
    private ArrayAdapter<String> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Shopping Lists");
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        /*String[] listItems = {"Item 1", "Item 2", "Item 3"};
        ListView listView = (ListView) view.findViewById(R.id.shopping_listView);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);

        listView.setAdapter(listViewAdapter);*/

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mShoppingList = (ListView) getView().findViewById(R.id.shopping_listView);
        mItemEdit = (EditText) getView().findViewById(R.id.item_editText);
        mAddButton = (Button) getView().findViewById(R.id.add_button);

        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        mShoppingList.setAdapter(mAdapter);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString();

                if (item.equals("")) {
                    Toast.makeText(getActivity(), "You can't add an empty item!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAdapter.add(item);
                    Log.d("Shopping Fragment", "ITEM ADDED: " + item);
                    mAdapter.notifyDataSetChanged();
                    mItemEdit.setText("");
                }

            }
        });
    }
}
