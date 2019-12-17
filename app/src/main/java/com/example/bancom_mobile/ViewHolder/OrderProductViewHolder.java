package com.example.bancom_mobile.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bancom_mobile.R;
import com.example.bancom_mobile.interfaces.ItemClickListener;

public class OrderProductViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public TextView txtProductName, txtProductDescription,txtProductPrice,txtProductQuantity;
    public ImageView imageView;
    public ItemClickListener listener;
    public CardView cardView;

    public OrderProductViewHolder(View itemView)
    {
        super(itemView);

        imageView =  itemView.findViewById(R.id.order_product_image);
        txtProductName = itemView.findViewById(R.id.order_product_name);
        txtProductQuantity = itemView.findViewById(R.id.order_product_quantity);
        txtProductPrice = itemView.findViewById(R.id.order_product_price);
        txtProductDescription = itemView.findViewById(R.id.order_product_description);
        cardView = itemView.findViewById(R.id.order_product_card);



    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
