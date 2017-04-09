package com.sid.voyage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sid.voyage.utils.MyUtil;

import java.util.Arrays;

public class CompanionActivity extends AppCompatActivity {

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView first = (ImageView)findViewById(R.id.first_user);
        Glide.with(this).load(MyUtil.getPref(this,"image")).into(first);
        getUser();

        final Button emailButton = (Button)findViewById(R.id.email);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));


            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void getUser()
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("users").orderByChild("id").equalTo(getIntent().getStringExtra("id"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {


                    Iterable<DataSnapshot> snap = dataSnapshot.getChildren();


                    for (DataSnapshot shot : snap) {

                        Log.d("Snap", "Shot " + shot);
                        email = shot.child("email").getValue().toString();
                        setSecondProfile(shot.child("name").getValue().toString(),shot.child("image").getValue().toString(),shot.child("gender").getValue().toString(),shot.child("country").getValue().toString(),shot.child("interest").getValue().toString());
                        break;
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    void setSecondProfile(String name, String image, String gender, String country, String interests)
    {
        ImageView secondUser = (ImageView)findViewById(R.id.second_user);
        Glide.with(this).load(image).into(secondUser);

        TextView nameSecond = (TextView)findViewById(R.id.name);
        nameSecond.setText(name);

        TextView genderTv = (TextView)findViewById(R.id.gender);
        genderTv.setText(gender);

        TextView location = (TextView)findViewById(R.id.location);

        location.setText(country);

        TextView interest = (TextView)findViewById(R.id.interests);
        interest.setText(interests);

    }

}
