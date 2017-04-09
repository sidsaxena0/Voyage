package com.sid.voyage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sid.voyage.utils.MyUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

public class BookTripActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{


    String day = "NaN";
    Spinner spinner;
    EditText fromCity,date,toCity;
    boolean shown;
    MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,new String[]{"How you are travlling?","Plane","Bus","Car","Train"});
        spinner.setAdapter(adapter);

        date = (EditText)findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        BookTripActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Choose trip date");


            }
        });

        fromCity = (EditText)findViewById(R.id.fromCity);
        toCity = (EditText)findViewById(R.id.toCity);
        Button find = (Button)findViewById(R.id.find);

        fromCity.setText(MySession.fromCity);
        toCity.setText(getIntent().getStringExtra("toCity"));

        fromCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BookTripActivity.this,SearchActivity.class).putExtra("return",true));

            }
        });


        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fromCity.getText().toString().equalsIgnoreCase(toCity.getText().toString()))
                    Toast.makeText(BookTripActivity.this, "Can't go to same place!", Toast.LENGTH_SHORT).show();
                else if (fromCity.getText().length()==0)
                    Toast.makeText(BookTripActivity.this, "Please select from city", Toast.LENGTH_SHORT).show();
                else if (day.equalsIgnoreCase("NaN"))
                    Toast.makeText(BookTripActivity.this, "Please select travel date", Toast.LENGTH_SHORT).show();
                else
                    save();


            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();

        fromCity.setText(MySession.fromCity);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home==item.getItemId())
        {
            onBackPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {


        date.setText(dayOfMonth+"-"+monthOfYear+"-"+year);
        day = date.getText().toString();


    }

    void save()
    {
       dialog =  new MaterialDialog.Builder(this)
                .title("Finding Companion")
                .content("Please wait while finding your travel companion....")
                .progress(true, 0)
                .show();


        String id = System.currentTimeMillis()+"";
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("trips").child(id).child("date").setValue(day);
        db.child("trips").child(id).child("id").setValue(id);
        db.child("trips").child(id).child("origin").setValue(fromCity.getText().toString());
        db.child("trips").child(id).child("destination").setValue(toCity.getText().toString());
        db.child("trips").child(id).child("by").setValue(spinner.getSelectedItem().toString());
        db.child("trips").child(id).child("userId").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.child("trips").child(id).child("image").setValue(getIntent().getStringExtra("image"));
        db.child("trips").child(id).child("interests").setValue(MyUtil.getPref(this,"interests"));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                findCompanion();

            }
        }, 2000);

    }


    void findCompanion()
    {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("trips").orderByChild("date").equalTo(day);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {


                    Iterable<DataSnapshot> snap = dataSnapshot.getChildren();

                    for (DataSnapshot shot : snap) {

                        Log.d("Snap", "Shot " + shot);

                        if (!shot.child("userId").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && shot.child("by").getValue().toString().equals(spinner.getSelectedItem().toString()))
                        {

                            if (shot.child("origin").getValue().toString().equals(fromCity.getText().toString()) && shot.child("destination").getValue().toString().equals(toCity.getText().toString()) && shot.hasChild("interests")) {
                                //interests

                                String interest = shot.child("interests").getValue().toString();

                                if (interest.contains(","))
                                {
                                    String[] interests = interest.split(",");

                                    int matched = 0;

                                    for (String interest1 : interests) {

//                                        if (Collections.singletonList(MyUtil.getPref(BookTripActivity.this, "interests")).contains(interest1)) {
//                                            matched++;
//                                        }
                                        if (Arrays.asList(MyUtil.getPref(BookTripActivity.this, "interests").split(",")).contains(interest1))
                                            matched++;



                                    }

                                    if (matched>1)
                                    {
                                        Log.d("Size","Matched "+matched);
                                        if (dialog!=null && dialog.isShowing())
                                            dialog.dismiss();

                                        //Found companion -- Show Profile
                                        Intent intent = new Intent(BookTripActivity.this,CompanionActivity.class);
                                        intent.putExtra("id",shot.child("userId").getValue().toString());
                                        startActivity(intent);

                                        break;


                                    }
                                }
                                else
                                    showNotifyDialogue();




                            }
                            else
                                showNotifyDialogue();


                        }
                        else
                            showNotifyDialogue();


                    }



                } catch (Exception e) {
                    e.printStackTrace();

                    if (dialog!=null && dialog.isShowing())
                        dialog.dismiss();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showNotifyDialogue() {


        if (!shown)

        {
            new MaterialDialog.Builder(this)
                    .title("Companion Not Found")
                    .content("Hi, there your trip has been saved successufully but we have not found any companion for your yet. Please wait for some time we will let you know as soon as someone matches with you trip criteria.")
                    .positiveText("Ok")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            finish();

                        }
                    })
                    .show();

            shown = true;


        }

        if (dialog!=null && dialog.isShowing())
            dialog.dismiss();

    }


}
