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
import com.sid.voyage.models.Hotel;

import java.util.ArrayList;

/**
 * Created by sid on 08/04/17.
 */

public class HotelsAdapter extends RecyclerView.Adapter<HotelsAdapter.ViewHolder>  {

    ArrayList<Hotel> hotels = new ArrayList<>();
    Context context;

    public HotelsAdapter(Context context, ArrayList<Hotel> hotels)
    {
        this.context = context;
        this.hotels = hotels;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_layout_row, parent, false);
        return new HotelsAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Glide.with(context).load(hotels.get(position).getImage()).into(holder.imageView);
        holder.name.setText(hotels.get(position).getName());

        holder.price.setText("₹"+hotels.get(position).getMinimumPrice());



    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,address,price;
        ImageView imageView;
        CardView layout;


        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.hotelName);
            price = (TextView)itemView.findViewById(R.id.price);
            address = (TextView)itemView.findViewById(R.id.address);
            imageView = (ImageView) itemView.findViewById(R.id.hotelImage);
            layout = (CardView) itemView.findViewById(R.id.card);

        }



    }

}
