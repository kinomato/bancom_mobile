package com.example.bancom_mobile.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bancom_mobile.CartActivity;
import com.example.bancom_mobile.Model.CartItem;

import java.util.ArrayList;

import com.example.bancom_mobile.Prevalent.Prevalent;
import com.example.bancom_mobile.R;
import com.example.bancom_mobile.ViewHolder.CartItemViewHolder;
import com.example.bancom_mobile.Viewmodel.CartViewModel;
import com.example.bancom_mobile.interfaces.ItemClickListener;
import com.example.bancom_mobile.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

import static com.example.bancom_mobile.ui.home.HomeFragment.currencyFormat;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemViewHolder>{

    public ItemClickListener clickListener;
    private ArrayList<CartItem> cartItems;
    private CartViewModel cartViewModel;

    public CartItemAdapter(ArrayList<CartItem> list, ItemClickListener listener, CartViewModel viewModel){
        cartItems = list;
        clickListener=listener;
        cartViewModel = viewModel;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_layout,parent,false);
        CartItemViewHolder holder = new CartItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, final int position) {
        CartItem model = cartItems.get(position);

        holder.txtProductName.setText(model.getName());
        holder.numberButton.setNumber(model.getQuantity().toString());
        holder.txtProductPrice.setText( currencyFormat(model.getPrice()) );
        Picasso.get().load(model.getImage()).into(holder.imageView);


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*System.out.println("delete" + position);*/
                /*Toast.makeText(v.getContext(), "deleted", Toast.LENGTH_SHORT).show();*/
                /*ArrayList<CartItem> cart = Paper.book().read("cart", new ArrayList<CartItem>());*/
                cartItems.remove(position);
                Paper.book().write("cart",cartItems);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,cartItems.size());
                calculateTotalMoney(cartItems);
                cartViewModel.setmTotalPrice(Prevalent.cartTotalPrice);
                /*onCreateDialog(v);*/
            }
        });
        holder.numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {


                CartItem item = cartItems.get(position);
                item.setQuantity(newValue);
                cartItems.set(position,item);
                Paper.book().write("cart",cartItems);
                calculateTotalMoney(cartItems);
                cartViewModel.setmTotalPrice(Prevalent.cartTotalPrice);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
    public Dialog onCreateDialog(final View v) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Delete this item ?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(v.getContext(), "deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    private void calculateTotalMoney(ArrayList<CartItem> cart) {
        int total = 0;
        if(cart.size() == 0){
            Prevalent.cartTotalPrice = 0;
        }
        for (int i = 0; i<cart.size();i++){
            CartItem cartItem = cart.get(i);
            int price = Integer.valueOf(cartItem.getPrice());
            int quantity = cartItem.getQuantity();
            int totalPrice = price * quantity;
            total +=totalPrice;
        }
        Prevalent.cartTotalPrice = total;
    }
}
