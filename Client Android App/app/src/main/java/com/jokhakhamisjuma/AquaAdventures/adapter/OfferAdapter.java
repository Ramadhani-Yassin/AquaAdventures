package com.jokhakhamisjuma.AquaAdventures.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.jokhakhamisjuma.AquaAdventures.R;
import com.jokhakhamisjuma.AquaAdventures.api.clients.RestClient;
import com.jokhakhamisjuma.AquaAdventures.model.Offer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {

    List<Offer> offerList;
    Context context;
    String Tag;

    public OfferAdapter(List<Offer> offerList, Context context) {
        this.offerList = offerList;
        this.context = context;
    }

    public OfferAdapter(List<Offer> offerList, Context context, String tag) {
        this.offerList = offerList;
        this.context = context;
        Tag = tag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_offer, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        Offer offer = offerList.get(position);
        Picasso.get().load(RestClient.BASE_URL +offer.getImage()).error(R.drawable.no_image).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {

        return offerList.size();


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.offer_image);
            progressBar = itemView.findViewById(R.id.progressbar);

        }
    }
}
