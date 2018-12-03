package com.example.eyee3.fridgemate;

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

public class FridgeFragment extends Fragment { //Fragment for displaying Fridge items

    public FridgeFragment() {

    }
    private SQLiteDatabase Fdatabase;
    private FridgeAdapter fAdapter;
    private FloatingActionButton floatAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Your Fridge");
        View view = inflater.inflate(R.layout.fragment_fridge, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FridgeDBHelper dbHelperF = new FridgeDBHelper(getActivity());
        Fdatabase = dbHelperF.getWritableDatabase();
        RecyclerView recyclerViewF = getView().findViewById(R.id.fridge_recycler);
        recyclerViewF.setLayoutManager(new LinearLayoutManager(getActivity()));
        fAdapter = new FridgeAdapter(getActivity(), getAllItems());
        recyclerViewF.setAdapter(fAdapter);

        //For swiping left/right to delete an item
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
        }).attachToRecyclerView(recyclerViewF);

        floatAdd = (FloatingActionButton) getView().findViewById(R.id.fabFridge);

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddFridgeFragment()).commit();
            }
        });
    }

    private Cursor getAllItems() { //Organizes RecyclerView items by their timestamp
        return Fdatabase.query(
                FridgeContract.FridgeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FridgeContract.FridgeEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    private void removeItem(long id) { //Deleting item from database
        Fdatabase.delete(FridgeContract.FridgeEntry.TABLE_NAME,
                FridgeContract.FridgeEntry._ID + "=" + id, null);
        fAdapter.swapCursor(getAllItems());
    }
}
