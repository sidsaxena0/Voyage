package com.sid.voyage;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sid.voyage.utils.MyUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);





        TextView textView = (TextView)findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"code.otf");
        textView.setTypeface(typeface);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (FirebaseAuth.getInstance().getCurrentUser()!=null)
                startActivity(new Intent(Splash.this,MainActivity.class));
                else
                    startActivity(new Intent(Splash.this,LoginActivity.class));


                finish();

            }
        }, 3000);


        getUserInfo(FirebaseAuth.getInstance().getCurrentUser().getUid());


    }




    private void getUserInfo(String id)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(id).addListenerForSingleValueEvent(

                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        try {
                            JSONObject object = new JSONObject();

                            object.put("name",dataSnapshot.child("name").getValue());
                            object.put("email",dataSnapshot.child("email").getValue());
                            object.put("country",dataSnapshot.child("country").getValue());
                            object.put("image",dataSnapshot.child("image").getValue());
                            object.put("username",dataSnapshot.child("username").getValue());
                            object.put("gender",dataSnapshot.child("gender").getValue());



                            MyUtil.saveUser(Splash.this,object.toString());
                            MyUtil.savePref(Splash.this,"image",dataSnapshot.child("image").getValue().toString());

                            startActivity(new Intent(Splash.this, MainActivity.class));
                            finish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Tag", "getUser:onCancelled", databaseError.toException());
                    }
                });


//        user.getReference("")
    }




}
