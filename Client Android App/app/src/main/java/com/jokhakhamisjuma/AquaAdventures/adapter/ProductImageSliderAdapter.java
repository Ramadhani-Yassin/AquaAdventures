package com.jokhakhamisjuma.AquaAdventures.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.jokhakhamisjuma.AquaAdventures.R;
import com.jokhakhamisjuma.AquaAdventures.api.clients.RestClient;
import com.jokhakhamisjuma.AquaAdventures.model.ProductImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProductImageSliderAdapter extends PagerAdapter {

    private Activity context;
    private LayoutInflater layoutInflater;
    private List<ProductImage> productImageList = new ArrayList<>();

    public ProductImageSliderAdapter(Activity context) {
        this.context = context;
    }

    public ProductImageSliderAdapter(Activity context, List<ProductImage> productImageList) {
        this.context = context;
        this.productImageList = productImageList;
    }

    @Override
    public int getCount() {
        return productImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_product_image_slider, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        Picasso.get().load(RestClient.BASE_URL+productImageList.get(position).getImage()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(context,R.style.full_screen_dialog);
                dialog.setContentView(R.layout.product_image);
                ImageView imageView1 = dialog.findViewById(R.id.image_popup);
                Picasso.get().load(RestClient.BASE_URL+productImageList.get(position).getImage()).into(imageView1);
                dialog.show();

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}