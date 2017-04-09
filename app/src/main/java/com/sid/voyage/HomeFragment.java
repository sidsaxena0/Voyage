package com.sid.voyage;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sid.voyage.adapters.SuggestionsAdapter;
import com.sid.voyage.models.Suggestion;
import com.sid.voyage.utils.ConnectionUtils;
import com.sid.voyage.utils.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    RecyclerView recyclerView;
    SuggestionsAdapter adapter;
    ArrayList<Suggestion> suggestions = new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        TextView textView = (TextView)view.findViewById(R.id.title);
        TextView subtitle = (TextView)view.findViewById(R.id.subtitle);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"code.otf");
        Typeface vonique = Typeface.createFromAsset(getActivity().getAssets(),"vonique.ttf");
        textView.setTypeface(typeface);
        subtitle.setTypeface(vonique);

        EditText search = (EditText)view.findViewById(R.id.search_box);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),SearchActivity.class));


            }
        });


        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapter = new SuggestionsAdapter(getContext(),suggestions);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        getRecommendedItems();




    }


    //Gets available items from api and displays them

    void getRecommendedItems()
    {

        ConnectionUtils utils = new ConnectionUtils(getContext());
        utils.makeGetRequest(new ResponseCallback() {
            @Override
            public void onResponse(String res) {

                try {
                    JSONObject object = new JSONObject(res);

                    JSONArray array = object.getJSONObject("data").getJSONArray("flight");

                    suggestions.clear();

                    for (int i = 0; i <array.length(); i++) {

                        Suggestion suggestion = new Suggestion();
                        suggestion.setCityId(array.getJSONObject(i).getString("cityId"));
                        suggestion.setCurrency(array.getJSONObject(i).getString("currency"));
                        suggestion.setName(array.getJSONObject(i).getString("name"));
                        suggestion.setCountry(array.getJSONObject(i).getString("countryName"));

                        byte ptext[] = new byte[0];
                        try {
                            ptext = array.getJSONObject(i).getString("data").getBytes("ISO-8859-1");
                            suggestion.setPrice(new String(ptext, "UTF-8"));

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();

                            suggestion.setPrice(array.getJSONObject(i).getString("price") +array.getJSONObject(i).getString("currency"));

                        }

                        suggestion.setPrice(array.getJSONObject(i).getString("data"));



                        suggestion.setImage(array.getJSONObject(i).getString("image"));

                        suggestions.add(suggestion);

                    }

                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(IOException e) {

            }

            @Override
            public void onNoNetwork() {

            }
        },getString(R.string.base_url)+"widgets/brand/inspire?product=1&apiKey=ixicode!2$");
    }


}
