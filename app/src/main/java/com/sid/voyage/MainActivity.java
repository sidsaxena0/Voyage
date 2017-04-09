package com.sid.voyage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sid.voyage.adapters.SuggestionsAdapter;
import com.sid.voyage.models.Suggestion;
import com.sid.voyage.utils.ConnectionUtils;
import com.sid.voyage.utils.MyUtil;
import com.sid.voyage.utils.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView home = (ImageView)findViewById(R.id.home);
        final ImageView profile = (ImageView)findViewById(R.id.profile);
        final ImageView trips = (ImageView)findViewById(R.id.trip);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();


                transaction1.replace(R.id.main_container, new HomeFragment());
                transaction1.addToBackStack(null);
                transaction1.commit();
                home.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
                profile.setColorFilter(Color.parseColor("#9d9d9d"));
                trips.setColorFilter(Color.parseColor("#9d9d9d"));


            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();


                transaction1.replace(R.id.main_container, new ProfileFragment());
                transaction1.addToBackStack(null);
                transaction1.commit();
                profile.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
                home.setColorFilter(Color.parseColor("#9d9d9d"));
                trips.setColorFilter(Color.parseColor("#9d9d9d"));


            }
        });


        trips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();


                transaction1.replace(R.id.main_container, new MyTripsFragment());
                transaction1.addToBackStack(null);
                transaction1.commit();
                trips.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
                home.setColorFilter(Color.parseColor("#9d9d9d"));
                profile.setColorFilter(Color.parseColor("#9d9d9d"));

            }



        });


        //init with home

        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();


        transaction1.replace(R.id.main_container, new HomeFragment());
        transaction1.addToBackStack(null);
        transaction1.commit();
        home.setColorFilter(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));





        checkInterests();


    }


    void checkInterests()
    {


        if (MyUtil.getPref(this,"interests").equalsIgnoreCase("na"))
        {

            String[] interests = new String[]{"Music","Games","Religion","Food","Dance","Politics"};




            //show dialogue
            new MaterialDialog.Builder(this)
                    .title("Choose your interests")
                    .items(interests)
                    .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {


                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                            for (int i = 0; i <text.length ; i++) {

                                databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("interests").child(""+i).child("id").setValue(i);
                                databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("interests").child(""+i).child("name").setValue(text[i]);

                            }


                            databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("interest").setValue(TextUtils.join(",",text));
                            MyUtil.savePref(MainActivity.this,"interests", TextUtils.join(",",text));



                            return true;
                        }
                    })
                    .positiveText("Choose")
                    .show();

        }
        else
        {
            //We are good to go ---let's check if match is available



        }


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
