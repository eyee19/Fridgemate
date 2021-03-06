package com.example.eyee3.fridgemate;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SavedFragment extends Fragment { //Fragment for displaying saved recipes
    private SQLiteDatabase sDatabase;
    private SavedAdapter sAdapter;

    public SavedFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Saved Recipes");
        View view = inflater.inflate(R.layout.fragment_saved, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SavedDBHelper dbHelper = new SavedDBHelper(getActivity());
        sDatabase = dbHelper.getWritableDatabase();
        RecyclerView recyclerView = getView().findViewById(R.id.saved_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sAdapter = new SavedAdapter(getActivity(), getAllItems());
        recyclerView.setAdapter(sAdapter);

        //Swiping left/right to delete saved recipes
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
    }

    private Cursor getAllItems() { //Displaying all saved recipes by timestamp in descending order
        return sDatabase.query(
                SavedContract.SavedEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                SavedContract.SavedEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    private void removeItem(long id) { //Deleting saved recipes
        sDatabase.delete(SavedContract.SavedEntry.TABLE_NAME,
                SavedContract.SavedEntry._ID + "=" + id, null);
        sAdapter.swapCursor(getAllItems());
    }
}



