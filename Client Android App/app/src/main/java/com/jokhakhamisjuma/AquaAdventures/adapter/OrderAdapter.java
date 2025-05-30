package com.jokhakhamisjuma.AquaAdventures.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jokhakhamisjuma.AquaAdventures.R;
import com.jokhakhamisjuma.AquaAdventures.api.clients.RestClient;
import com.jokhakhamisjuma.AquaAdventures.model.Order;
import com.jokhakhamisjuma.AquaAdventures.model.OrderItem;
import com.jokhakhamisjuma.AquaAdventures.model.OrdersResult;
import com.jokhakhamisjuma.AquaAdventures.model.User;
import com.jokhakhamisjuma.AquaAdventures.util.localstorage.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jokhakhamisjuma.AquaAdventures.activity.BaseActivity.TAG;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    List<Order> orderList;
    Context context;
    int pQuantity = 1;
    String _subtotal, _price, _quantity;
    LocalStorage localStorage;
    Gson gson;
    User user;
    String token;
    List<OrderItem> orderItemList = new ArrayList<>();
    OrderItemAdapter orderItemAdapter;
    RecyclerView recyclerView;

    public OrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;


        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_order, parent, false);

        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Order order = orderList.get(position);
        holder.orderId.setText("#" + order.getId());
        holder.date.setText(order.getDate());
        holder.total.setText(order.getTotal());
        holder.status.setText(order.getStatus());
        holder.status.setText(order.getStatus());
        holder.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrderItemModal(order);
            }
        });


    }

    private void openOrderItemModal(Order order) {
        final Dialog dialog = new Dialog(context, R.style.FullScreenDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.orderdetails_dialog);
        Gson gson = new Gson();
        localStorage = new LocalStorage(context);
        user = gson.fromJson(localStorage.getUserLogin(), User.class);
        token = user.getToken();
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder_id(order.getId());
        orderItem.setToken(token);

        Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
        recyclerView = dialog.findViewById(R.id.order_list);

        Call<OrdersResult> call = RestClient.getRestService(context).getOrderItems(orderItem);
        call.enqueue(new Callback<OrdersResult>() {
            @Override
            public void onResponse(Call<OrdersResult> call, Response<OrdersResult> response) {
                if(response!=null && response.body()!=null){
                    if(response.body().getCode()==200){
                        orderItemList = response.body().getOrderItemList();
                        orderItemAdapter = new OrderItemAdapter(orderItemList, context);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(orderItemAdapter);
                    }

                }


            }

            @Override
            public void onFailure(Call<OrdersResult> call, Throwable t) {
                Log.d(TAG, "errorResponse:==>" + t.getMessage());
            }
        });


        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {

        return orderList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, date, total, status, viewDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.order_id);
            date = itemView.findViewById(R.id.date);
            total = itemView.findViewById(R.id.total_amount);
            status = itemView.findViewById(R.id.status);
            viewDetails = itemView.findViewById(R.id.viewDetails);

        }
    }
}
