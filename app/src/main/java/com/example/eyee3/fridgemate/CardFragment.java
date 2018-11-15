package com.example.eyee3.fridgemate;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class CardFragment extends Fragment {
    ArrayList<RecipeModel> listitems = new ArrayList<>();
    EditText searchInput;
    Button addSearch;
    Button clearAll;
    Button goSearch;
    TextView searching;
    RecyclerView MyRecyclerView;
    static final String API_URL = "http://www.recipepuppy.com/api/?i=";
    String Recipes[] = {"Recipe 1","Recipe 2","Recipe 3","Recipe 4","Recipe 5","Recipe 6","Recipe 7"};
    int  Images[] = {R.drawable.fridge_splash, R.drawable.fridge_splash, R.drawable.fridge_splash, R.drawable.fridge_splash,
            R.drawable.fridge_splash, R.drawable.fridge_splash, R.drawable.fridge_splash};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeList();
        getActivity().setTitle("Recipes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        searchInput = (EditText) view.findViewById(R.id.searchBox);
        addSearch = (Button) view.findViewById(R.id.plus);
        clearAll = (Button) view.findViewById(R.id.clearAll);
        goSearch = (Button) view.findViewById(R.id.search);
        searching = (TextView) view.findViewById(R.id.searchingFor);

        addSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searching.append(searchInput.getText().toString() + ",");
                searchInput.setText("");
            }
        });

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput.setText("");
                searching.setText("");
            }
        });

        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });
    }

    public void initializeList() {
        listitems.clear();

        for(int i =0;i<7;i++){
            RecipeModel item = new RecipeModel();
            item.setCardName(Recipes[i]);
            item.setImageResourceId(Images[i]);
            item.setIsfav(0);
            item.setIsturned(0);
            listitems.add(item);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
            shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int)likeImageView.getTag();
                    if( id == R.drawable.ic_bookmark_darkgray_24dp) {
                        likeImageView.setTag(R.drawable.ic_bookmark_skyblue_24dp);
                        likeImageView.setImageResource(R.drawable.ic_bookmark_skyblue_24dp);
                        Toast.makeText(getActivity(),titleTextView.getText()+" added to favorites",Toast.LENGTH_SHORT).show();
                    }
                    else  {
                        likeImageView.setTag(R.drawable.ic_bookmark_darkgray_24dp);
                        likeImageView.setImageResource(R.drawable.ic_bookmark_darkgray_24dp);
                        Toast.makeText(getActivity(),titleTextView.getText()+" removed from favorites",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(coverImageView.getId())
                            + '/' + "drawable" + '/' + getResources().getResourceEntryName((int)coverImageView.getTag()));
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                }
            });
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
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.titleTextView.setText(list.get(position).getCardName());
            holder.coverImageView.setImageResource(list.get(position).getImageResourceId());
            holder.coverImageView.setTag(list.get(position).getImageResourceId());
            holder.likeImageView.setTag(R.drawable.ic_bookmark_darkgray_24dp);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

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
            }

            //searching.setText(response);
            Log.d("URL PASSED", response);


            try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                //String requestID = object.getString("requestId");
                //int likelihood = object.getInt("likelihood");
                //JSONArray photos = object.getJSONArray("photos");

                JSONArray results = object.getJSONArray("results");
                Log.d("CardFragment", "RESULTS ARRAY: " + results);

                for(int n = 0; n < results.length(); n++)
                {
                    JSONObject recipe = results.getJSONObject(n);

                    String title = recipe.getString("title");
                    String link = recipe.getString("href");
                    String ingredients = recipe.getString("ingredients");
                    String thumbnail = recipe.getString("thumbnail");

                    Log.d("CardFragment","INDIVIDUAL: " + title);
                    Log.d("CardFragment","INDIVIDUAL: " + link);
                    Log.d("CardFragment","INDIVIDUAL: " + ingredients);
                    Log.d("CardFragment","INDIVIDUAL: " + thumbnail);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
