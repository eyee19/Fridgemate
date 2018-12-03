package com.example.eyee3.fridgemate;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CardFragment extends Fragment { //Fragment for displaying the recipes
    ArrayList<RecipeModel> listitems = new ArrayList<>();
    EditText searchInput;
    ImageButton clearAll;
    ImageButton goSearch;
    TextView resultsForBox;
    TextView searching;
    static View.OnClickListener myOnClickListener;
    boolean addBool = false;
    RecyclerView MyRecyclerView;
    static final String API_URL = "http://www.recipepuppy.com/api/?i=";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Recipes"); //Sets the title of the fragment on screen
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        myOnClickListener = new MyOnClickListener(getActivity());
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        MyRecyclerView.setAdapter(new MyAdapter(listitems));
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        searchInput = (EditText) view.findViewById(R.id.searchBox);
        clearAll = (ImageButton) view.findViewById(R.id.clearAll);
        goSearch = (ImageButton) view.findViewById(R.id.search);
        resultsForBox = (TextView) view.findViewById(R.id.resultsForBox);
        searching = (TextView) view.findViewById(R.id.searchingFor);

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput.setText("");
                searching.setText("");
                Toast.makeText(getActivity(), "Cleared Search", Toast.LENGTH_SHORT).show();
                addBool = false; //addBool is used to determine whether or not the user has input an item yet
            }
        });

        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo(); //Checking if there is an internet connection
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (searchInput.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter an item to add", Toast.LENGTH_SHORT).show();
                }
                else if (isConnected == true) {
                    searching.setText(searchInput.getText().toString());
                    searchInput.setText("");
                    new RetrieveFeedTask().execute(); //Begins async task
                    addBool = false;
                }
                else if (isConnected == false) {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Add an item to search first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView ingredientsTextView;
        public TextView labelTextView;
        public ImageView thumbImage;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            ingredientsTextView = (TextView) v.findViewById(R.id.ingredientsTextView);
            labelTextView = (TextView) v.findViewById(R.id.labelTextView);
            thumbImage = (ImageView) v.findViewById(R.id.picture);
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<RecipeModel> list;

        public MyAdapter(ArrayList<RecipeModel> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            view.setOnClickListener(CardFragment.myOnClickListener);

            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final SQLiteDatabase sDatabase;
            SavedDBHelper dbHelperS = new SavedDBHelper(getActivity());
            sDatabase = dbHelperS.getWritableDatabase();
            holder.titleTextView.setText(list.get(position).getCardName());
            holder.ingredientsTextView.setText(list.get(position).getIngredientsList());
            holder.thumbImage.setImageBitmap(list.get(position).getPictureLink());
            holder.titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //onClickListeners for when user clicks on recipe card, opens in browser
                    String url = list.get(position).getLink();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            holder.ingredientsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = list.get(position).getLink();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            holder.thumbImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String savedTitle = list.get(position).getCardName(); //Long click the picture and it will save the recipe
                    String savedLink = list.get(position).getLink();
                    Toast.makeText(getActivity(), "\"" + savedTitle + "\" added to saved recipes", Toast.LENGTH_SHORT).show();

                    ContentValues cv = new ContentValues();
                    cv.put(SavedContract.SavedEntry.COLUMN_NAMES, savedTitle);
                    cv.put(SavedContract.SavedEntry.COLUMN_LINK, savedLink);

                    sDatabase.insert(SavedContract.SavedEntry.TABLE_NAME, null, cv);
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        private Exception exception;

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... urls) {
            String fullSearch = searching.getText().toString();
            String removeSpace = fullSearch.replaceAll("\\s+","");

            try {
                URL url = new URL(API_URL + removeSpace);
                Log.d("CardFragment", "THE URL: " + url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); //Opening the connection
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
                Toast.makeText(getActivity(), "Error: no response. Check your internet connection", Toast.LENGTH_SHORT).show();
            }

            try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue(); //Retrieving results from API as a JSON object
                JSONArray results = object.getJSONArray("results");

                listitems.clear(); //Clears any previous searches
                for(int n = 0; n < results.length(); n++) //Loops through API response and stores in database
                {
                    JSONObject recipe = results.getJSONObject(n);

                    String title = recipe.getString("title");
                    String link = recipe.getString("href");
                    String ingredients = recipe.getString("ingredients");
                    final String thumbnailpic = recipe.getString("thumbnail");

                    String trimmedTitle = title.replaceAll("(\\r|\\n|\\t|&amp;)", ""); //Removes unnecessary new line characters
                    String evenMoreTrim = trimmedTitle.trim();

                    final RecipeModel item = new RecipeModel();
                    item.setCardName(evenMoreTrim);
                    item.setLink(link);
                    item.setIngredientsList(ingredients);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() { //Converting the URL into a Bitmap and resizing it
                            try  {
                                Bitmap bitmapUpdated = BitmapFactory.decodeStream((InputStream)new URL(thumbnailpic).getContent());
                                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmapUpdated, 360, 360, false);
                                item.setPictureLink(resizedBitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    listitems.add(item);
                    MyRecyclerView.getAdapter().notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
