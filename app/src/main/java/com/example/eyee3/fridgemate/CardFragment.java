package com.example.eyee3.fridgemate;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.Calendar;

public class CardFragment extends Fragment {
    ArrayList<RecipeModel> listitems = new ArrayList<>();
    EditText searchInput;
    ImageButton addSearch;
    ImageButton clearAll;
    ImageButton goSearch;
    TextView searching;
    static View.OnClickListener myOnClickListener;
    boolean addBool = false;
    RecyclerView MyRecyclerView;
    static final String API_URL = "http://www.recipepuppy.com/api/?i=";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializeList();
        getActivity().setTitle("Recipes");
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
        addSearch = (ImageButton) view.findViewById(R.id.plus);
        clearAll = (ImageButton) view.findViewById(R.id.clearAll);
        goSearch = (ImageButton) view.findViewById(R.id.search);
        searching = (TextView) view.findViewById(R.id.searchingFor);

        addSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchInput.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter an item to add", Toast.LENGTH_SHORT).show();
                }
                else {
                    searching.append(searchInput.getText().toString() + ",");
                    searchInput.setText("");
                    addBool = true;
                }
            }
        });

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput.setText("");
                searching.setText("");
                addBool = false;
            }
        });

        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if (addBool == true && isConnected == true) {
                    new RetrieveFeedTask().execute();
                    //searching.setText("");
                    addBool = false;
                }
                else if (isConnected == false) {
                    Toast.makeText(getActivity(), "Error: no internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Add An Item to Search First", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*public void initializeList() {
        listitems.clear();

        for(int i = 0; i < 11; i++){
            RecipeModel item = new RecipeModel();
            item.setCardName(Recipes[i]);
            item.setIsturned(0);
            listitems.add(item);
        }
    }*/

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
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            view.setOnClickListener(CardFragment.myOnClickListener);

            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.titleTextView.setText(list.get(position).getCardName());
            holder.ingredientsTextView.setText(list.get(position).getIngredientsList());
            holder.titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
        ImageView thumb = getView().findViewById(R.id.picture);
        private Exception exception;

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... urls) {
            String fullSearch = searching.getText().toString();

            try {
                URL url = new URL(API_URL + fullSearch);
                Log.d("CardFragment", "THE URL: " + url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();

                JSONArray results = object.getJSONArray("results");

                listitems.clear();
                for(int n = 0; n < results.length(); n++)
                {
                    JSONObject recipe = results.getJSONObject(n);

                    String title = recipe.getString("title");
                    String link = recipe.getString("href");
                    String ingredients = recipe.getString("ingredients");
                    String thumbnailpic = recipe.getString("thumbnail");

                    String trimmedTitle = title.replaceAll("(\\r|\\n|\\t)", "");
                    String evenMoreTrim = trimmedTitle.trim();
                    //new DownloadImageTask(thumb).execute(thumbnailpic);
                    /*if (thumb == null) {
                        Picasso.with(getActivity()).load("https://i.imgur.com/DvpvklR.png").into(thumb);
                    }
                    else {
                        Picasso.with(getActivity()).load(thumbnailpic).into(thumb);
                    }*/

                    RecipeModel item = new RecipeModel();
                    item.setCardName(evenMoreTrim);
                    item.setLink(link);
                    item.setIngredientsList(ingredients);
                    listitems.add(item);
                    MyRecyclerView.getAdapter().notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
