package com.jokhakhamisjuma.AquaAdventures.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jokhakhamisjuma.AquaAdventures.api.clients.RestClient;
import com.google.gson.Gson;
import com.jokhakhamisjuma.AquaAdventures.R;
import com.jokhakhamisjuma.AquaAdventures.activity.BaseActivity;
import com.jokhakhamisjuma.AquaAdventures.activity.ProductViewActivity;
import com.jokhakhamisjuma.AquaAdventures.interfaces.AddorRemoveCallbacks;
import com.jokhakhamisjuma.AquaAdventures.model.Cart;
import com.jokhakhamisjuma.AquaAdventures.model.Product;
import com.jokhakhamisjuma.AquaAdventures.util.localstorage.LocalStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    List<Product> productList;
    Context context;
    String Tag;
    int pQuantity = 1;
    LocalStorage localStorage;
    Gson gson;
    List<Cart> cartList = new ArrayList<>();
    String _quantity, _price, _attribute, _subtotal;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public ProductAdapter(List<Product> productList, Context context, String tag) {
        this.productList = productList;
        this.context = context;
        Tag = tag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_products, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Product product = productList.get(position);
        localStorage = new LocalStorage(context);
        gson = new Gson();
        cartList = ((BaseActivity) context).getCartList();
        holder.title.setText(product.getName());

        if (product.getPrice() != null && product.getPrice().length() != 0 && product.getDiscount() != null && product.getDiscount().length() != 0) {

            double M = Double.parseDouble(product.getPrice());
            double S = Double.parseDouble(product.getDiscount());
            double discount = M - S;

            int disPercent = (int) Math.round((discount / M) * 100);

            if (disPercent > 1) {
                holder.offer.setText(disPercent + "% OFF");
            } else {
                holder.offer.setVisibility(View.GONE);
            }

        } else {
            holder.offer.setVisibility(View.GONE);
        }
        holder.attribute.setText(product.getAttribute());
        holder.currency.setText(product.getCurrency());
        if (product.getDiscount() != null && product.getDiscount().length() != 0) {
            holder.price.setText(product.getDiscount());
            holder.org_price.setText(product.getPrice());
            holder.org_price.setPaintFlags(holder.org_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {
            holder.price.setText(product.getPrice());
            holder.org_price.setVisibility(View.GONE);
        }

        Picasso.get()
                .load(RestClient.BASE_URL + product.getImages().get(0).getImage())
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error : ", e.getMessage());
                    }
                });


        if (product.getDiscount() == null || product.getDiscount().length() == 0) {
            holder.offer.setVisibility(View.GONE);
        }

        if (!cartList.isEmpty()) {
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {
                    holder.subTotal.setVisibility(View.VISIBLE);
                    holder.quantity.setText(cartList.get(i).getQuantity());
                    _quantity = cartList.get(i).getQuantity();
                    if (product.getDiscount() != null && product.getDiscount().length() != 0) {
                        _price = product.getDiscount();
                    } else {
                        _price = product.getPrice();
                    }

                    _subtotal = String.valueOf(Double.parseDouble(_price) * Integer.parseInt(_quantity));
                    holder.subTotal.setText(_quantity + "X" + _price + "= TZS." + _subtotal);
                    Log.d("Tag : ", cartList.get(i).getId() + "-->" + product.getId());
                }
            }
        } else {

            holder.quantity.setText("0");
        }

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pQuantity = Integer.parseInt(holder.quantity.getText().toString());
                int total_item = Integer.parseInt(holder.quantity.getText().toString());
                total_item++;
                Log.d("TAG", "onClick: " + total_item);

                holder.quantity.setText(total_item + "");
                holder.subTotal.setVisibility(View.VISIBLE);

                if (product.getDiscount() != null && product.getDiscount().length() != 0) {
                    _price = product.getDiscount();
                } else {
                    _price = product.getPrice();
                }

                _subtotal = String.valueOf(Double.parseDouble(_price) * total_item);
                holder.subTotal.setText(total_item + "X" + _price + "= TZS." + _subtotal);

                if (cartList.size() == 0 || pQuantity == 0) {

                        Cart cart = new Cart(product.getId(), product.getName(), RestClient.BASE_URL + product.getImages().get(0).getImage(), product.getCurrency(), _price, product.getAttribute(), total_item + "", _subtotal);
                        cartList = ((BaseActivity) context).getCartList();
                        cartList.add(cart);
                        String cartStr = gson.toJson(cartList);
                        //Log.d("CART", cartStr);
                        localStorage.setCart(cartStr);
                        ((AddorRemoveCallbacks) context).onAddProduct();
                        notifyItemChanged(position);

                } else {
                    for (int i = 0; i < cartList.size(); i++) {

                        if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {
                            _subtotal = String.valueOf(Double.parseDouble(holder.price.getText().toString()) * total_item);
                            cartList.get(i).setQuantity(holder.quantity.getText().toString());
                            cartList.get(i).setSubTotal(_subtotal);
                            holder.subTotal.setText(total_item + "X" + holder.price.getText().toString() + "= TZS." + _subtotal);
                            String cartStr = gson.toJson(cartList);
                            //Log.d("CART", cartStr);
                            localStorage.setCart(cartStr);
                            notifyItemChanged(position);
                        }
                    }
                }


            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pQuantity = Integer.parseInt(holder.quantity.getText().toString());
                int total_item = Integer.parseInt(holder.quantity.getText().toString());

                if(total_item==0){
                   return;
                }
                total_item--;
                Log.d("TAG", "onClick: " + total_item);
                holder.quantity.setText(total_item+"");
                if (total_item == 0) {
                    holder.subTotal.setVisibility(View.GONE);
                    cartList = ((BaseActivity) context).getCartList();
                    for (Cart cart : cartList) {
                        if (cart.getId().equalsIgnoreCase(product.getId())) {
                            cartList.remove(cart);
                            String cartStr = gson.toJson(cartList);
                            localStorage.setCart(cartStr);
                            notifyItemRangeChanged(position, cartList.size());
                            notifyDataSetChanged();
                            ((AddorRemoveCallbacks) context).onRemoveProduct();
                            break;
                        }
                    }
                    return;
                }

                if (product.getDiscount() != null && product.getDiscount().length() != 0) {
                    _price = product.getDiscount();
                } else {
                    _price = product.getPrice();
                }

                _subtotal = String.valueOf(Double.parseDouble(_price) * total_item);
                holder.subTotal.setText(total_item + "X" + _price + "= TZS." + _subtotal);


                if (total_item > 0) {

                    for (int i = 0; i < cartList.size(); i++) {
                        if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {

                            _subtotal = String.valueOf(Double.parseDouble(holder.price.getText().toString()) * total_item);
                            cartList.get(i).setQuantity(holder.quantity.getText().toString());
                            cartList.get(i).setSubTotal(_subtotal);
                            holder.subTotal.setText(total_item + "X" + holder.price.getText().toString() + "= TZS." + _subtotal);
                            String cartStr = gson.toJson(cartList);
                            //Log.d("CART", cartStr);
//                            ((AddorRemoveCallbacks) context).onRemoveProduct();
                            localStorage.setCart(cartStr);
                            notifyItemChanged(position);
                        }
                    }

                }

            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, ProductActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);*/
                Toast.makeText(context, "Product Clicked", Toast.LENGTH_LONG).show();
            }
        });

/*
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.addToCart.setVisibility(View.GONE);
                holder.subTotal.setVisibility(View.VISIBLE);


                if (product.getDiscount() != null && product.getDiscount().length() != 0) {
                    _price = product.getDiscount();
                } else {
                    _price = product.getPrice();
                }
                _quantity = holder.quantity.getText().toString();
                _attribute = product.getAttribute();

                if (Integer.parseInt(_quantity) != 0) {
                    _subtotal = String.valueOf(Double.parseDouble(_price) * Integer.parseInt(_quantity));
                    holder.subTotal.setText(_quantity + "X" + _price + "= TZS." + _subtotal);
                    if (context instanceof ProductActivity) {
                        Cart cart = new Cart(product.getId(), product.getName(), product.getImage(), product.getCurrency(), _price, _attribute, _quantity, _subtotal);
                        cartList = ((BaseActivity) context).getCartList();
                        cartList.add(cart);
                        String cartStr = gson.toJson(cartList);
                        //Log.d("CART", cartStr);
                        localStorage.setCart(cartStr);
                        ((AddorRemoveCallbacks) context).onAddProduct();
                        notifyItemChanged(position);
                    }
                } else {
                    Toast.makeText(context, "Please Add Quantity", Toast.LENGTH_SHORT).show();
                }


            }
        });
*/

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


    public void updateCart() {

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
        TextView title;
        ProgressBar progressBar;
        CardView cardView;
        TextView offer, currency, price, org_price, quantity, attribute, subTotal;
        Button plus, minus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            progressBar = itemView.findViewById(R.id.progressbar);
            cardView = itemView.findViewById(R.id.card_view);
            offer = itemView.findViewById(R.id.product_discount);
            currency = itemView.findViewById(R.id.product_currency);
            price = itemView.findViewById(R.id.product_price);
            org_price = itemView.findViewById(R.id.original_price);
            quantity = itemView.findViewById(R.id.quantity);
//            addToCart = itemView.findViewById(R.id.add_to_cart);
            attribute = itemView.findViewById(R.id.product_attribute);
            plus = itemView.findViewById(R.id.quantity_plus);
            minus = itemView.findViewById(R.id.quantity_minus);
            subTotal = itemView.findViewById(R.id.sub_total);
        }
    }
}
