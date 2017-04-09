package com.sid.voyage.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sid.voyage.R;
import com.sid.voyage.models.Trip;

import java.util.ArrayList;

/**
 * Created by sid on 09/04/17.
 */

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {

    Context context;
    ArrayList<Trip> trips = new ArrayList<>();



    public TripsAdapter(Context context, ArrayList<Trip> trips)
    {
        this.context = context;
        this.trips = trips;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_layout, parent, false);
        return new TripsAdapter.ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (trips.get(position).getBy().equalsIgnoreCase("train"))
            holder.transportMode.setImageResource(R.drawable.ic_directions_transit_black_24dp);
        else if (trips.get(position).getBy().equalsIgnoreCase("bus"))
            holder.transportMode.setImageResource(R.drawable.ic_directions_bus_black_24dp);
        else if (trips.get(position).getBy().equalsIgnoreCase("car"))
            holder.transportMode.setImageResource(R.drawable.ic_directions_car_black_24dp);
        else
            holder.transportMode.setImageResource(R.drawable.ic_flight_black_24dp);

        holder.from.setText(trips.get(position).getOrigin());
        holder.toCity.setText(trips.get(position).getDestination());
        holder.date.setText(trips.get(position).getDate());
        Glide.with(context).load(trips.get(position).getImage()).into(holder.bgImage);


    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView from,toCity,date;
        ImageView transportMode,bgImage;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);

            from = (TextView)itemView.findViewById(R.id.from);
            date = (TextView)itemView.findViewById(R.id.date);
            toCity = (TextView)itemView.findViewById(R.id.toCity);
            transportMode = (ImageView)itemView.findViewById(R.id.transportMode);
            bgImage = (ImageView)itemView.findViewById(R.id.image);
            card = (CardView) itemView.findViewById(R.id.card);


        }



    }
}
