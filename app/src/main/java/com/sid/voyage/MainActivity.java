package com.sid.voyage;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.sid.voyage.adapters.SuggestionsAdapter;
import com.sid.voyage.pojo.Suggestion;
import com.sid.voyage.utils.ConnectionUtils;
import com.sid.voyage.utils.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    SuggestionsAdapter adapter;
    ArrayList<Suggestion> suggestions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView)findViewById(R.id.title);
        TextView subtitle = (TextView)findViewById(R.id.subtitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"code.otf");
        Typeface vonique = Typeface.createFromAsset(getAssets(),"vonique.ttf");
        textView.setTypeface(typeface);
        subtitle.setTypeface(vonique);

        EditText search = (EditText) findViewById(R.id.search_box);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,SearchActivity.class));


            }
        });


        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new SuggestionsAdapter(this,suggestions);
        recyclerView.setAdapter(adapter);
        getRecommendedItems();

    }


    //Gets available items from api and displays them

    void getRecommendedItems()
    {

        ConnectionUtils utils = new ConnectionUtils(this);
        utils.makeGetRequest(new ResponseCallback() {
            @Override
            public void onResponse(String res) {

                try {
                    JSONObject object = new JSONObject(res);

                    JSONArray array = object.getJSONObject("data").getJSONArray("flight");

                    suggestions.clear();

                    for (int i = 0; i <array.length(); i++) {

                        Suggestion suggestion = new Suggestion();
                        suggestion.setCityId(array.getJSONObject(i).getString("cityId"));
                        suggestion.setCurrency(array.getJSONObject(i).getString("currency"));
                        suggestion.setName(array.getJSONObject(i).getString("name"));
                        suggestion.setCountry(array.getJSONObject(i).getString("countryName"));
                        suggestion.setPrice(array.getJSONObject(i).getString("price"));
                        suggestion.setImage(array.getJSONObject(i).getString("image"));

                        suggestions.add(suggestion);

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
        },getString(R.string.base_url)+"widgets/brand/inspire?product=1&apiKey=ixicode!2$");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
