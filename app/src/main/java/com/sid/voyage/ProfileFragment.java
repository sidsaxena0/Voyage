package com.sid.voyage;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sid.voyage.utils.MyUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    TextView nameTv,bdayTv,countryTv,genderTv,interestTv,usernameTv,emailTv;
    ImageView imageView;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        nameTv = (TextView)view.findViewById(R.id.name);
        bdayTv = (TextView)view.findViewById(R.id.bday);
        countryTv = (TextView)view.findViewById(R.id.location);
        genderTv = (TextView)view.findViewById(R.id.gender);
        interestTv = (TextView)view.findViewById(R.id.interests);
        usernameTv = (TextView)view.findViewById(R.id.username);
        emailTv = (TextView)view.findViewById(R.id.email);
        imageView = (ImageView)view.findViewById(R.id.profilePic);
        CardView logoutCard = (CardView)view.findViewById(R.id.logoutCard);
        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),LoginActivity.class));

            }
        });


        interestTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String[] interests = new String[]{"Music","Games","Religion","Food","Dance","Politics"};


                //show dialogue
                new MaterialDialog.Builder(getContext())
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
                                MyUtil.savePref(getContext(),"interests", TextUtils.join(",",text));



                                return true;
                            }
                        })
                        .positiveText("Choose")
                        .show();

            }
        });


        getProfile();



    }



    void getProfile()
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("users").orderByChild("id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {


                    Iterable<DataSnapshot> snap = dataSnapshot.getChildren();


                    for (DataSnapshot shot : snap) {

                        Log.d("Snap", "Shot " + shot);

                        setProfile(shot.child("name").getValue().toString(),shot.child("email").getValue().toString(),shot.child("username").getValue().toString(),shot.child("image").getValue().toString(),shot.child("gender").getValue().toString(),shot.child("country").getValue().toString(),shot.child("interest").getValue().toString());
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


    void setProfile(String name, String email, String username, String image, String gender, String country, String interest)
    {
        nameTv.setText(name);
        emailTv.setText(email);
        usernameTv.setText(username);
        Glide.with(this).load(image).into(imageView);
        genderTv.setText(gender);
        countryTv.setText(country);
        interestTv.setText(interest);
    }
}
