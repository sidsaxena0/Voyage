package com.sid.voyage.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sid.voyage.CityActivity;
import com.sid.voyage.MySession;
import com.sid.voyage.R;
import com.sid.voyage.models.City;

import java.util.ArrayList;


/**
 * Created by sid on 08/04/17.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<City>  cities  = new ArrayList<>();
    boolean returnData;

    public CityAdapter(Activity context, ArrayList<City> cities, boolean returnData)
    {
        this.context = context;
        this.cities = cities;
        this.returnData = returnData;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_layout, parent, false);
        return new CityAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.name.setText(cities.get(position).getName()+", "+cities.get(position).getState()+", "+cities.get(position).getCountry());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!returnData)
                {
                    Intent intent = new Intent(context, CityActivity.class);
                    intent.putExtra("id",cities.get(position).getCityId());
                    intent.putExtra("city",cities.get(position).getName());
                    intent.putExtra("country",cities.get(position).getCountry());
                    context.startActivity(intent);

                }
                else
                {
                    MySession.fromCity = cities.get(position).getName();
                    context.onBackPressed();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.text);
            layout = (RelativeLayout)itemView.findViewById(R.id.layout);

        }



    }

}
