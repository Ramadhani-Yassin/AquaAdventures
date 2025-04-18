package com.jokhakhamisjuma.AquaAdventures.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.jokhakhamisjuma.AquaAdventures.R;
import com.jokhakhamisjuma.AquaAdventures.adapter.ProductImageSliderAdapter;
import com.jokhakhamisjuma.AquaAdventures.helper.Converter;
import com.jokhakhamisjuma.AquaAdventures.model.Cart;
import com.jokhakhamisjuma.AquaAdventures.model.ProductImage;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductViewActivity extends BaseActivity {
    private static int cart_count = 0;
    public TextView quantity, inc, dec;
    String _id, _title, _image, _description, _price, _currency, _discount, _attribute;
    TextView id, title, description, price, org_price, currency, discount, attribute;
    ProgressBar progressBar;
    LinearLayout addToCart, share;
    RelativeLayout quantityLL;
    List<Cart> cartList = new ArrayList<>();
    int cartId;
    Cart cart;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    List<ProductImage>productImages=new ArrayList<>();
    private int dotscount;
    private ImageView[] dots;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        Intent intent = getIntent();

        _id = intent.getStringExtra("id");
        _title = intent.getStringExtra("title");
        _image = intent.getStringExtra("image");
        _description = intent.getStringExtra("description");
        _price = intent.getStringExtra("price");
        _currency = intent.getStringExtra("currency");
        _discount = intent.getStringExtra("discount");
        _attribute = intent.getStringExtra("attribute");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        changeActionBarTitle(getSupportActionBar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        //upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        cart_count = cartCount();

        title = findViewById(R.id.apv_title);
        description = findViewById(R.id.apv_description);
        currency = findViewById(R.id.apv_currency);
        price = findViewById(R.id.apv_price);
        org_price = findViewById(R.id.apv_org_price);
        attribute = findViewById(R.id.apv_attribute);
        discount = findViewById(R.id.apv_discount);
        progressBar = findViewById(R.id.progressbar);
        addToCart = findViewById(R.id.add_to_cart_ll);
        share = findViewById(R.id.apv_share);
        quantityLL = findViewById(R.id.quantity_rl);
        quantity = findViewById(R.id.quantity);
        inc = findViewById(R.id.quantity_plus);
        dec = findViewById(R.id.quantity_minus);
        viewPager = findViewById(R.id.viewPager);
        sliderDotspanel = findViewById(R.id.SliderDots);
        if(_image!=null){
            Type type = new TypeToken<List<ProductImage>>(){}.getType();
            productImages = gson.fromJson(_image, type);

            setupBannerSlider();
        }

        cartList = getCartList();
        title.setText(_title);

        if (_description != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                description.setText(Html.fromHtml(_description, Html.FROM_HTML_MODE_COMPACT));
            } else {
                description.setText(Html.fromHtml(_description));
            }
        }

        if (_discount != "" && _discount.length() > 0) {
            price.setText(_discount);
            org_price.setText(_price);
            org_price.setPaintFlags(org_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            price.setText(_price);
        }
        currency.setText(_currency);
        attribute.setText(_attribute);
        discount.setText(_discount);
        Log.d(TAG, "Discount : " + _discount);


        if (_price != null && _price.length() != 0 && _price != "" && _discount != null && _discount.length() != 0 && _discount != "") {
            double M = Double.parseDouble(_price);
            double S = Double.parseDouble(_discount);
            double discount_m = M - S;
            int disPercent = (int) Math.round((discount_m / M) * 100);

            if (disPercent > 1) {
                discount.setText(disPercent + "% OFF");
            } else {
                discount.setVisibility(View.GONE);
            }

        } else {
            discount.setVisibility(View.GONE);
        }

        if (!cartList.isEmpty()) {
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getId().equalsIgnoreCase(_id)) {
                    addToCart.setVisibility(View.GONE);
                    quantityLL.setVisibility(View.VISIBLE);
                    quantity.setText(cartList.get(i).getQuantity());
                    cartId = i;

                }
            }
        }


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEntry = _image + "\n" + _title + "\n" + _description + "\n" + _attribute + "-" + _currency + _price + "(" + _discount + ")";

                Intent textShareIntent = new Intent(Intent.ACTION_SEND);
                textShareIntent.putExtra(Intent.EXTRA_TEXT, userEntry);
                textShareIntent.setType("text/plain");
                startActivity(textShareIntent);
            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _price = price.getText().toString();

                cart = new Cart(_id, _title, _image, _currency, _price, _attribute, "1", _price);
                cartList.add(cart);
                String cartStr = gson.toJson(cartList);
                //Log.d("CART", cartStr);
                localStorage.setCart(cartStr);
                onAddProduct();
                addToCart.setVisibility(View.GONE);
                quantityLL.setVisibility(View.VISIBLE);
            }
        });


        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _price = price.getText().toString();


                // int total_item = Integer.parseInt(cartList.get(cartId).getQuantity());
                int total_item = Integer.parseInt(quantity.getText().toString());
                total_item++;
                Log.d("totalItem", total_item + "");
                quantity.setText(total_item + "");
                String subTotal = String.valueOf(Double.parseDouble(_price) * total_item);
                cartList.get(cartId).setQuantity(quantity.getText().toString());
                cartList.get(cartId).setSubTotal(subTotal);
                cartList.get(cartId).setAttribute(attribute.getText().toString());
                cartList.get(cartId).setPrice(_price);
                String cartStr = gson.toJson(cartList);
                //Log.d("CART", cartStr);
                localStorage.setCart(cartStr);
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _price = price.getText().toString();

                //int total_item = Integer.parseInt(quantity.getText().toString());
                int total_item = Integer.parseInt(quantity.getText().toString());
                if (total_item != 1) {
                    total_item--;
                    quantity.setText(total_item + "");
                    Log.d("totalItem", total_item + "");
                    String subTotal = String.valueOf(Double.parseDouble(_price) * total_item);


                    cartList.get(cartId).setQuantity(quantity.getText().toString());
                    cartList.get(cartId).setSubTotal(subTotal);
                    cartList.get(cartId).setAttribute(attribute.getText().toString());
                    cartList.get(cartId).setPrice(_price);
                    String cartStr = gson.toJson(cartList);
                    //Log.d("CART", cartStr);
                    localStorage.setCart(cartStr);
                }
            }
        });


    }

    private void changeActionBarTitle(ActionBar actionBar) {
        // Create a LayoutParams for TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        TextView tv = new TextView(getApplicationContext());
        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(null, Typeface.BOLD);
        // Set text to display in TextView
        tv.setText("Trip Details"); // ActionBar title text
        tv.setTextSize(20);

        // Set the text color of TextView to red
        // This line change the ActionBar title text color
        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Set the ActionBar display option
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // Finally, set the newly created TextView as ActionBar custom view
        actionBar.setCustomView(tv);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                onBackPressed();
                return true;

            case R.id.cart_action:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(ProductViewActivity.this, cart_count, R.drawable.ic_shopping_basket));
        return true;
    }


    @Override
    public void onAddProduct() {
        cart_count++;
        invalidateOptionsMenu();

    }

    @Override
    public void onRemoveProduct() {
        cart_count--;
        invalidateOptionsMenu();

    }


    private void setupBannerSlider() {

        ProductImageSliderAdapter viewPagerAdapter = new ProductImageSliderAdapter(this, productImages);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];


        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(getApplicationContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
