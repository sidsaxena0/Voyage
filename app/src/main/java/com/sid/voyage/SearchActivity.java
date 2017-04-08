package com.sid.voyage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.sid.voyage.adapters.CityAdapter;
import com.sid.voyage.pojo.City;
import com.sid.voyage.pojo.Suggestion;
import com.sid.voyage.utils.ConnectionUtils;
import com.sid.voyage.utils.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ArrayList<City> cities = new ArrayList<>();
    CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CityAdapter(this,cities);
        recyclerView.setAdapter(adapter);

        ImageView voice = (ImageView)findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //start voice search



            }
        });


        EditText search = (EditText)findViewById(R.id.search_box);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //Search text changed now query

                getResult(s.toString());

            }
        });




    }



    void getResult(String query)
    {

        ConnectionUtils utils = new ConnectionUtils(this);
        utils.makeGetRequest(new ResponseCallback() {
            @Override
            public void onResponse(String res) {

                try {

                    JSONArray array = new JSONArray(res);

                    cities.clear();

                    for (int i = 0; i <array.length(); i++) {

                        City city = new City();
                        city.setCityId(array.getJSONObject(i).getString("_id"));
                        city.setName(array.getJSONObject(i).getString("text").substring(0,1).toUpperCase()+array.getJSONObject(i).getString("text").substring(1));
                        city.setState(array.getJSONObject(i).getString("st"));
                        city.setCountry(array.getJSONObject(i).getString("co"));
                        city.setaLong(Double.parseDouble(array.getJSONObject(i).getString("lon")));
                        city.setLat(Double.parseDouble(array.getJSONObject(i).getString("lat")));

                        cities.add(city);

                    }

                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(IOException e) {

            }

            @Override
            public void onNoNetwork() {

            }
        },"http://build2.ixigo.com/action/content/zeus/autocomplete?searchFor=tpAutoComplete&neCategories=City&query="+query);

    }



}
