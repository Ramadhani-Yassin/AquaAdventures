package com.jokhakhamisjuma.AquaAdventures.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jokhakhamisjuma.AquaAdventures.R;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images = {R.drawable.welcome_slider1, R.drawable.welcome_slider2, R.drawable.welcome_slider3};

    private String[] title = {
            "Book Your Boat Easily",
            "Pick Your Destination",
            "Start Your Adventure"
    };

    private String[] description = {
            "Reserve a boat instantly from your phone — simple, fast, and secure.",
            "Browse iconic locations around Zanzibar and choose where you want to go.",
            "Sit back, relax, and enjoy your unforgettable trip across Zanzibar’s waters."
    };


    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_slider, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);
        TextView titleTV = view.findViewById(R.id.title);
        titleTV.setText(title[position]);
        TextView desc = view.findViewById(R.id.description);
        desc.setText(description[position]);



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