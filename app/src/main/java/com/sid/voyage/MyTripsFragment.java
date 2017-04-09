package com.sid.voyage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sid.voyage.adapters.TripsAdapter;
import com.sid.voyage.models.Trip;
import com.sid.voyage.utils.MyUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyTripsFragment extends Fragment {


    ArrayList<Trip> trips = new ArrayList<>();
    TripsAdapter adapter;


    public MyTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_trips, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TripsAdapter(getContext(),trips);
        recyclerView.setAdapter(adapter);

        getTrips();


    }


    void getTrips()
    {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("trips").orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {


                    Iterable<DataSnapshot> snap = dataSnapshot.getChildren();


                    for (DataSnapshot shot : snap) {

                        Log.d("Snap", "Shot " + shot);
                        Trip trip = new Trip();
                        trip.setBy(shot.child("by").getValue().toString());
                        trip.setDestination(shot.child("destination").getValue().toString());
                        trip.setOrigin(shot.child("origin").getValue().toString());
                        trip.setDate(shot.child("date").getValue().toString());
                        trip.setInterests(shot.child("interests").getValue().toString());
                        trip.setUserId(shot.child("userId").getValue().toString());
                        trip.setImage(shot.child("image").getValue().toString());

                        trips.add(trip);

                    }

                    Collections.reverse(trips);

                    //coz data is coming in reverse order somehow

                    adapter.notifyDataSetChanged();



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
