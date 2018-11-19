package com.example.eyee3.fridgemate;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
    private SQLiteDatabase mDatabase;
    private GroceryAdapter mAdapter;
    private EditText mItemEdit;
    private FloatingActionButton addList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Your Shopping List");
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        GroceryDBHelper dbHelper = new GroceryDBHelper(getActivity());
        mDatabase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = getView().findViewById(R.id.shopping_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new GroceryAdapter(getActivity(), getAllItems());
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

        mItemEdit = (EditText) getView().findViewById(R.id.item_editText);
        addList = (FloatingActionButton) getView().findViewById(R.id.fabList);

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString();
                ContentValues cv = new ContentValues();
                cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, item);

                if (item.equals("") || mItemEdit.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), "You can't add an empty item!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
                    mAdapter.swapCursor(getAllItems());
                    mItemEdit.setText("");
                }
            }
        });
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    private void removeItem(long id) {
        mDatabase.delete(GroceryContract.GroceryEntry.TABLE_NAME,
                GroceryContract.GroceryEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());
    }
}
