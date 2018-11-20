package com.example.eyee3.fridgemate;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FridgeAdapter extends RecyclerView.Adapter<FridgeAdapter.FridgeViewHolder> {
    private Context fContext;
    private Cursor fCursor;

    public FridgeAdapter(Context context, Cursor cursor) {
        fContext = context;
        fCursor = cursor;
    }
    public class FridgeViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView addedText;
        public TextView expText;
        public TextView quantity;

        public FridgeViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.fridgeName);
            addedText = itemView.findViewById(R.id.dateAddedBox);
            expText = itemView.findViewById(R.id.expBox);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }

    @Override
    public FridgeAdapter.FridgeViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(fContext);
        View view = inflater.inflate(R.layout.fridge_item, parent, false);
        return new FridgeAdapter.FridgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FridgeAdapter.FridgeViewHolder fridgeViewHolder, int i) {
        if (!fCursor.moveToPosition(i)) {
            return;
        }

        String nameF = fCursor.getString(fCursor.getColumnIndex(FridgeContract.FridgeEntry.COLUMN_NAMEF));
        String dateAdded = fCursor.getString(fCursor.getColumnIndex(FridgeContract.FridgeEntry.COLUMN_DATE_ADDED));
        String exp = fCursor.getString(fCursor.getColumnIndex(FridgeContract.FridgeEntry.COLUMN_DATE_EXP));
        String quan = fCursor.getString(fCursor.getColumnIndex(FridgeContract.FridgeEntry.COLUMN_QUANTITY));
        long id = fCursor.getLong(fCursor.getColumnIndex(GroceryContract.GroceryEntry._ID));

        fridgeViewHolder.nameText.setText(nameF);
        fridgeViewHolder.addedText.setText(dateAdded);
        fridgeViewHolder.expText.setText(exp);
        fridgeViewHolder.quantity.setText(quan);
        fridgeViewHolder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return fCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (fCursor != null) {
            fCursor.close();
        }

        fCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
