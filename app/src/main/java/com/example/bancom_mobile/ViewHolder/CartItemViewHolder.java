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

public class CartItemViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public TextView txtProductName, txtProductDescription,txtProductPrice;
    public ImageView imageView;
    public ItemClickListener listener;
    public CardView cardView;
    public ElegantNumberButton numberButton;
    public Button deleteButton;

    public CartItemViewHolder(View itemView)
    {
        super(itemView);

        imageView =  itemView.findViewById(R.id.cart_product_image);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        numberButton = itemView.findViewById(R.id.cart_product_number_btn);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        cardView = itemView.findViewById(R.id.cart_product_card);
        deleteButton = itemView.findViewById(R.id.cart_delete_btn);


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
