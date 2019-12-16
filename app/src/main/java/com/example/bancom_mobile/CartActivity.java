package com.example.bancom_mobile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bancom_mobile.Adapter.CartItemAdapter;
import com.example.bancom_mobile.Model.CartItem;
import com.example.bancom_mobile.Prevalent.Prevalent;
import com.example.bancom_mobile.ViewHolder.CartItemViewHolder;
import com.example.bancom_mobile.Viewmodel.CartViewModel;
import com.example.bancom_mobile.interfaces.ItemClickListener;

import java.util.ArrayList;

import io.paperdb.Paper;

import static com.example.bancom_mobile.ui.home.HomeFragment.currencyFormat;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView txtTotalPrice;
    private ElegantNumberButton elegantNumberButton;
    private CartItemAdapter cartItemAdapter;
    private ItemClickListener listener;
    private CartViewModel cartViewModel;
    private String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        nextProcessBtn= findViewById(R.id.next_process_btn);
        txtTotalPrice = findViewById(R.id.total_price);
        ArrayList<CartItem> cartItemArrayList = Paper.book().read("cart", new ArrayList<CartItem>());
        cartItemAdapter = new CartItemAdapter(cartItemArrayList,listener,cartViewModel);
        calculateTotalMoney(cartItemArrayList);
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cartItemAdapter);

        cartViewModel.setmTotalPrice(Prevalent.cartTotalPrice);

        cartViewModel.getmTotalPrice().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                price = integer.toString();
                txtTotalPrice.setText(currencyFormat(price));
            }
        });

        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Prevalent.cartTotalPrice == 0){
                    return;
                }
                Intent intent= new Intent(CartActivity.this,ConfirmOrderActivity.class);
                intent.putExtra("price",price);
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("Cart");

    }

    private void calculateTotalMoney(ArrayList<CartItem> cart) {
        int total = 0;
        if(cart.size() == 0){
            return;
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

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
