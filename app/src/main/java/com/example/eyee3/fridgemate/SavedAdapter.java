package com.example.eyee3.fridgemate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.SavedViewHolder> {
    private Context sContext;
    private Cursor sCursor;

    public SavedAdapter(Context context, Cursor cursor) {
        sContext = context;
        sCursor = cursor;
    }
    public class SavedViewHolder extends RecyclerView.ViewHolder {
        public TextView savedName;

        public SavedViewHolder(View itemView) {
            super(itemView);
            savedName = itemView.findViewById(R.id.savedName);
        }
    }

    @Override
    public SavedAdapter.SavedViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(sContext);
        View view = inflater.inflate(R.layout.saved_item, parent, false);
        return new SavedAdapter.SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedAdapter.SavedViewHolder savedViewHolder, int i) {
        if (!sCursor.moveToPosition(i)) {
            return;
        }

        String nameS = sCursor.getString(sCursor.getColumnIndex(SavedContract.SavedEntry.COLUMN_NAMES));
        final String linkS = sCursor.getString(sCursor.getColumnIndex(SavedContract.SavedEntry.COLUMN_LINK));
        long id = sCursor.getLong(sCursor.getColumnIndex(SavedContract.SavedEntry._ID));

        savedViewHolder.savedName.setText(nameS);
        savedViewHolder.itemView.setTag(id);

        savedViewHolder.savedName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = linkS;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                sContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (sCursor != null) {
            sCursor.close();
        }

        sCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
