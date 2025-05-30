package com.jokhakhamisjuma.AquaAdventures.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jokhakhamisjuma.AquaAdventures.api.clients.RestClient;
import com.google.gson.Gson;
import com.jokhakhamisjuma.AquaAdventures.R;
import com.jokhakhamisjuma.AquaAdventures.activity.BaseActivity;
import com.jokhakhamisjuma.AquaAdventures.activity.MainActivity;
import com.jokhakhamisjuma.AquaAdventures.activity.ProductViewActivity;
import com.jokhakhamisjuma.AquaAdventures.interfaces.AddorRemoveCallbacks;
import com.jokhakhamisjuma.AquaAdventures.model.Cart;
import com.jokhakhamisjuma.AquaAdventures.model.Product;
import com.jokhakhamisjuma.AquaAdventures.util.localstorage.LocalStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.MyViewHolder> {

    List<Product> productList;
    Context context;
    String Tag;
    LocalStorage localStorage;
    Gson gson;
    List<Cart> cartList = new ArrayList<>();
    String _quantity, _price, _attribute, _subtotal;

    public PopularProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public PopularProductAdapter(List<Product> productList, Context context, String tag) {
        this.productList = productList;
        this.context = context;
        Tag = tag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        if (Tag.equalsIgnoreCase("Home")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_popular_home_products, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_popuular_products, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final Product product = productList.get(position);
        localStorage = new LocalStorage(context);
        gson = new Gson();
        cartList = ((BaseActivity) context).getCartList();

        holder.title.setText(product.getName());
        if (product.getDiscount() != null && product.getDiscount().length() != 0) {
            holder.price.setText(product.getDiscount());
            holder.org_price.setText(product.getPrice());
            holder.org_price.setPaintFlags(holder.org_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {
            holder.price.setText(product.getPrice());
            holder.org_price.setVisibility(View.GONE);
        }
        holder.attribute.setText(product.getAttribute());
        Picasso.get().load(RestClient.BASE_URL + product.getImages().get(0).getImage()).error(R.drawable.no_image).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Log.d("Error : ", e.getMessage());
            }
        });


        if (!cartList.isEmpty()) {
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {
                    holder.shopNow.setVisibility(View.GONE);
                    holder.quantity_ll.setVisibility(View.VISIBLE);
                    holder.quantity.setText(cartList.get(i).getQuantity());
                    Log.d("Tag : ", cartList.get(i).getId() + "-->" + product.getId());
                }
            }
        }


        holder.shopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.shopNow.setVisibility(View.GONE);
                holder.quantity_ll.setVisibility(View.VISIBLE);
                if (product.getDiscount() != null && product.getDiscount().length() != 0) {
                    _price = product.getDiscount();
                } else {
                    _price = product.getPrice();
                }
                holder.currency.setText(product.getCurrency());
                _quantity = holder.quantity.getText().toString();
                _attribute = product.getAttribute();
                _subtotal = String.valueOf(Double.parseDouble(_price) * Integer.parseInt(_quantity));

                if (context instanceof MainActivity) {
                    Cart cart = new Cart(product.getId(), product.getName(), RestClient.BASE_URL + product.getImages().get(0).getImage(), product.getCurrency(), _price, _attribute, _quantity, _subtotal);
                    cartList = ((BaseActivity) context).getCartList();
                    cartList.add(cart);

                    String cartStr = gson.toJson(cartList);
                    //Log.d("CART", cartStr);
                    localStorage.setCart(cartStr);
                    ((AddorRemoveCallbacks) context).onAddProduct();
                    notifyItemChanged(position);
                }
            }
        });


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < cartList.size(); i++) {
                    if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {
                        int total_item = Integer.parseInt(cartList.get(i).getQuantity());
                        total_item++;
                        Log.d("totalItem", total_item + "");
                        holder.quantity.setText(total_item + "");
                        _subtotal = String.valueOf(Double.parseDouble(holder.price.getText().toString()) * total_item);
                        cartList.get(i).setQuantity(holder.quantity.getText().toString());
                        cartList.get(i).setSubTotal(_subtotal);
                        String cartStr = gson.toJson(cartList);
                        //Log.d("CART", cartStr);
                        localStorage.setCart(cartStr);
                    }
                }


            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(holder.quantity.getText().toString()) != 1) {
                    for (int i = 0; i < cartList.size(); i++) {
                        if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {
                            int total_item = Integer.parseInt(holder.quantity.getText().toString());

                            total_item--;
                            holder.quantity.setText(total_item + "");
                            Log.d("totalItem", total_item + "");

                            _subtotal = String.valueOf(Double.parseDouble(holder.price.getText().toString()) * total_item);
                            cartList.get(i).setQuantity(holder.quantity.getText().toString());
                            cartList.get(i).setSubTotal(_subtotal);
                            String cartStr = gson.toJson(cartList);
                            //Log.d("CART", cartStr);
                            localStorage.setCart(cartStr);
                        }
                    }

                }


            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductViewActivity.class);
                intent.putExtra("id", product.getId());
                intent.putExtra("title", product.getName());
                intent.putExtra("image", gson.toJson(product.getImages()));
                intent.putExtra("price", product.getPrice());
                intent.putExtra("currency", product.getCurrency());
                intent.putExtra("attribute", product.getAttribute());
                intent.putExtra("discount", product.getDiscount());
                intent.putExtra("description", product.getDescription());


                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {

        return productList.size();

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, attribute, currency, price, org_price, shopNow;
        ProgressBar progressBar;
        LinearLayout quantity_ll;
        TextView plus, minus, quantity;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            attribute = itemView.findViewById(R.id.product_attribute);
            price = itemView.findViewById(R.id.product_price);
            org_price = itemView.findViewById(R.id.original_price);
            currency = itemView.findViewById(R.id.product_currency);
            shopNow = itemView.findViewById(R.id.shop_now);
            progressBar = itemView.findViewById(R.id.progressbar);
            quantity_ll = itemView.findViewById(R.id.quantity_ll);
            quantity = itemView.findViewById(R.id.quantity);
            plus = itemView.findViewById(R.id.quantity_plus);
            minus = itemView.findViewById(R.id.quantity_minus);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }
}
