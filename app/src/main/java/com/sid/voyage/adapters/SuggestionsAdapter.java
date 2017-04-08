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
import com.sid.voyage.pojo.Suggestion;

import java.util.ArrayList;

/**
 * Created by sid on 08/04/17.
 */

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Suggestion> suggestions = new ArrayList<>();


    public SuggestionsAdapter(Context context, ArrayList<Suggestion> suggestions)
    {
        this.context = context;
        this.suggestions = suggestions;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_row_layout, parent, false);
        return new SuggestionsAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.heading.setText(suggestions.get(position).getName());
        holder.price.setText(suggestions.get(position).getPrice());
        Glide.with(context).load(suggestions.get(position).getImage()).into(holder.placeImage);

    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView heading,price;
        ImageView placeImage;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);

            heading = (TextView)itemView.findViewById(R.id.title);
            price = (TextView)itemView.findViewById(R.id.price);
            placeImage = (ImageView)itemView.findViewById(R.id.item_image);
            card = (CardView) itemView.findViewById(R.id.card);


        }



    }

}
