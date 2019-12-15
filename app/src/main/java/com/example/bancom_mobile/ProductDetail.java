package com.example.bancom_mobile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bancom_mobile.Model.CartItem;
import com.example.bancom_mobile.Model.Products;
import com.example.bancom_mobile.Prevalent.Prevalent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.paperdb.Paper;

public class ProductDetail extends AppCompatActivity {

    private String prodName, prodPrice, prodDescription, prodImage, prodId;
    private TextView name, price, description;
    private ImageView imageView;
    private Button addCart;
    private ElegantNumberButton amountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("Product detail");

        prodId = getIntent().getStringExtra("prodId");
        prodName = getIntent().getStringExtra("prodName");
        prodPrice = getIntent().getStringExtra("prodPrice");
        prodDescription = getIntent().getStringExtra("prodDescription");
        prodImage = getIntent().getStringExtra("prodImage");


        imageView = findViewById(R.id.productDetail_image);
        name = findViewById(R.id.productDetail_prodName);
        price = findViewById(R.id.productDetail_prodPrice);
        description = findViewById(R.id.productDetail_description);
        addCart = findViewById(R.id.productDetail_addCart);
        amountButton = findViewById(R.id.productDetail_amount_btn);

        name.setText(prodName);
        price.setText(prodPrice);
        description.setText(prodDescription);
        Picasso.get().load(prodImage).into(imageView);

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
    }

    private void addingToCartList() {

        ArrayList<CartItem> tempCart;
        CartItem cartItem = new CartItem(prodId, prodName, prodDescription, prodPrice, prodImage,
                Integer.parseInt(amountButton.getNumber()));
        /*try {
            tempCart = Paper.book().read("cart");
        }
        catch (Exception e) {
            tempCart = Paper.book().read("cart", new ArrayList<CartItem>());
        }*/
        tempCart = Paper.book().read("cart", new ArrayList<CartItem>());
        tempCart.add(cartItem);
        Paper.book().write("cart", tempCart);
        Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}