package com.example.eyee3.fridgemate;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddFridgeFragment extends Fragment { //Fragment for adding a new item to the Fridge
    private SQLiteDatabase Fdatabase;
    private TextInputEditText nameBox;
    private TextInputEditText dateBox;
    private TextInputEditText expirationBox;
    private TextInputEditText quantityBox;
    private Button cancel;
    private Button addItem;
    DatePickerDialog picker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Add To Fridge");
        View view = inflater.inflate(R.layout.fragment_addfridge, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FridgeDBHelper dbHelperF = new FridgeDBHelper(getActivity()); //Accessing the database
        Fdatabase = dbHelperF.getWritableDatabase();

        nameBox = (TextInputEditText) getView().findViewById(R.id.nameInput);
        dateBox = (TextInputEditText) getView().findViewById(R.id.dateInput);
        expirationBox = (TextInputEditText) getView().findViewById(R.id.expirationInput);
        quantityBox = (TextInputEditText) getView().findViewById(R.id.quantityInput);
        cancel = (Button) getView().findViewById(R.id.cancel);
        addItem = (Button) getView().findViewById(R.id.addItemToFridge);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = nameBox.getText().toString();
                String dateAdded = dateBox.getText().toString();
                String exp = expirationBox.getText().toString();
                String quan = quantityBox.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); //Setting date format

                ContentValues cv = new ContentValues();
                cv.put(FridgeContract.FridgeEntry.COLUMN_NAMEF, item);
                cv.put(FridgeContract.FridgeEntry.COLUMN_DATE_ADDED, dateAdded);
                cv.put(FridgeContract.FridgeEntry.COLUMN_DATE_EXP, exp);
                cv.put(FridgeContract.FridgeEntry.COLUMN_QUANTITY, quan);

                try {
                    Date add = sdf.parse(dateAdded);
                    Date expire = sdf.parse(exp);
                    if(expire.before(add)) { //Making sure expiration date isn't before the date it was added
                        Toast.makeText(getActivity(), "Expiration date cannot be before added date!", Toast.LENGTH_SHORT).show();
                    }
                    else if (item.equals("") || nameBox.getText().toString().trim().length() == 0) {
                        Toast.makeText(getActivity(), "You can't add an empty item!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Fdatabase.insert(FridgeContract.FridgeEntry.TABLE_NAME, null, cv);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FridgeFragment()).commit();
                    }
                } catch(ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        dateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance(); //New calander instance
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(getActivity(), R.style.AlertDialogCustom,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateBox.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        expirationBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(getActivity(), R.style.AlertDialogCustom,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                expirationBox.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FridgeFragment()).commit();
            }
        });
    }
}
