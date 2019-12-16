package com.example.bancom_mobile.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancom_mobile.R;
import com.example.bancom_mobile.interfaces.ItemClickListener;

public class OrderViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public TextView txtOrderName, txtOrderPhone, txtOrderTotal, txtOrderDateTime, txtOrderAddressCity;
    public ItemClickListener listener;
    public CardView cardView;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderName =(TextView) itemView.findViewById(R.id.order_user_name);
        txtOrderPhone =(TextView) itemView.findViewById(R.id.order_phone);
        txtOrderAddressCity =(TextView) itemView.findViewById(R.id.order_address_city);
        txtOrderDateTime =(TextView) itemView.findViewById(R.id.order_time_date);
        txtOrderTotal =(TextView) itemView.findViewById(R.id.order_total_value);
        cardView = itemView.findViewById(R.id.order_card);

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
