package com.example.bancom_mobile.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancom_mobile.R;
import com.example.bancom_mobile.interfaces.ItemClickListener;


public class ProductViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener
{

    public TextView txtProductName, txtProductDescription,txtProductPrice;
    public ImageView imageView;
    public ItemClickListener listener;
    public CardView cardView;

    public ProductViewHolder(View itemView)
    {
        super(itemView);

        imageView = itemView.findViewById(R.id.product_image);
        txtProductName = itemView.findViewById(R.id.product_name);
        txtProductDescription = itemView.findViewById(R.id.product_description);
        txtProductPrice = itemView.findViewById(R.id.product_price);
        cardView = itemView.findViewById(R.id.product_card);

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
