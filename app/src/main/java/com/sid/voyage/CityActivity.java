package com.sid.voyage;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sid.voyage.adapters.HotelsAdapter;
import com.sid.voyage.models.Hotel;
import com.sid.voyage.utils.ConnectionUtils;
import com.sid.voyage.utils.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CityActivity extends AppCompatActivity {

    ImageView cityImage;
    TextView countryName,cityName,desc;
    HotelsAdapter adapter;
    ArrayList<Hotel> hotels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cityImage = (ImageView)findViewById(R.id.image);
        countryName = (TextView)findViewById(R.id.countryName);
        cityName = (TextView)findViewById(R.id.cityName);
        desc = (TextView)findViewById(R.id.desc);

        countryName.setText(getIntent().getStringExtra("country"));
        cityName.setText(getIntent().getStringExtra("city"));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Take to plan trip



            }
        });

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new HotelsAdapter(this,hotels);
        recyclerView.setAdapter(adapter);

        getCityDetails(getIntent().getStringExtra("id"));
        getHotels(getIntent().getStringExtra("id"));





    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    void getTemperature(String lat, String aLong)
    {
        ConnectionUtils utils = new ConnectionUtils(this);
        utils.makeGetRequest(new ResponseCallback() {
            @Override
            public void onResponse(String res) {

                try {

                    JSONObject object = new JSONObject(res);

                    setTemperatureCard(object.getString("name"),object.getJSONArray("weather").getJSONObject(0).getString("description"),object.getJSONObject("main").getString("temp_min"));

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
        },"http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&units=metric&lon="+aLong+"&appid=fcf4f53e3366122d298f3eda8b4d7a2a");


    }


    void getHotels(String id)
    {
        ConnectionUtils utils = new ConnectionUtils(this);
        utils.makeGetRequest(new ResponseCallback() {
            @Override
            public void onResponse(String res) {

                try {

                    JSONObject object = new JSONObject(res);

                    JSONArray data = object.getJSONObject("data").getJSONArray("accommodation");


                    for (int i = 0; i <data.length(); i++) {

                        Hotel hotel = new Hotel();
                        hotel.setAddress(data.getJSONObject(i).getString("address"));
                        hotel.setCity(data.getJSONObject(i).getString("cityName"));
                        hotel.setCountry(data.getJSONObject(i).getString("countryName"));
                        if(data.getJSONObject(i).has("description"))
                        hotel.setDescription(data.getJSONObject(i).getString("description"));
                        hotel.setCityId(data.getJSONObject(i).getString("cityId"));
                        hotel.setId(data.getJSONObject(i).getString("id"));
                        hotel.setState(data.getJSONObject(i).getString("stateName"));
                        hotel.setName(data.getJSONObject(i).getString("name"));
                        hotel.setMinimumPrice(data.getJSONObject(i).getString("minimumPrice"));
                        hotel.setLongitude(data.getJSONObject(i).getString("longitude"));
                        hotel.setLatitude(data.getJSONObject(i).getString("latitude"));
                        if(data.getJSONObject(i).has("keyImageUrl"))
                            hotel.setImage(data.getJSONObject(i).getString("keyImageUrl"));
                        hotel.setXid(data.getJSONObject(i).getString("xid"));


                        hotels.add(hotel);


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
        },"http://build2.ixigo.com/api/v3/namedentities/city/"+id+"/categories?apiKey=ixicode!2$&type=places%20to%20visit,accommodation,things%20to%20do&skip=1&limit=100");


    }




    void getCityDetails(String id)
    {

        ConnectionUtils utils = new ConnectionUtils(this);
        utils.makeGetRequest(new ResponseCallback() {
            @Override
            public void onResponse(String res) {

                try {

                    JSONObject object = new JSONObject(res);

                    JSONObject data = object.getJSONObject("data");

                    Glide.with(CityActivity.this).load(data.getString("keyImageUrl")).into(cityImage);
                    desc.setText(Html.fromHtml(data.getString("description")));
                    getTemperature(data.getString("latitude"),data.getString("longitude"));

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
        },"http://build2.ixigo.com/api/v3/namedentities/id/"+id+"?apiKey=ixicode!2$");


    }



    void setTemperatureCard(String city, String cond, String temp)
    {
        TextView cityName = (TextView)findViewById(R.id.city_name);
        ImageView weatherImage = (ImageView)findViewById(R.id.weather_image);
        TextView condition = (TextView)findViewById(R.id.condition);
        TextView temperature = (TextView)findViewById(R.id.temperature);
        CardView weather = (CardView)findViewById(R.id.temperatureCard);

       cityName.setText(city);
        condition.setText(cond);
        temperature.setText(temp+"°");

        if (Float.parseFloat(temp)>20)
        {
            Glide.with(this).load(R.drawable.sun).into(weatherImage);
            weather.setCardBackgroundColor(Color.parseColor("#FF6F00"));
        }
        else if (Float.parseFloat(temp)>30)
        {
            Glide.with(this).load(R.drawable.summer).into(weatherImage);
            weather.setCardBackgroundColor(Color.parseColor("#E65100"));
        }
        else if (Float.parseFloat(temp)<10)
        {
            Glide.with(this).load(R.drawable.snowflake).into(weatherImage);
            weather.setCardBackgroundColor(Color.parseColor("#607D8B"));
        }
        else
        {
            //Don't have much time otherwise would have used all possible weathers

            Glide.with(this).load(R.drawable.cloudy).into(weatherImage);
            weather.setCardBackgroundColor(Color.parseColor("#4CAF50"));
        }




    }
}
