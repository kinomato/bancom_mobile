package com.example.bancom_mobile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class ProductDetail extends AppCompatActivity {

    private String prodName, prodPrice, prodDescription, prodImage;
    private TextView name, price, description;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("Product detail");

        prodName = getIntent().getStringExtra("prodName");
        prodPrice = getIntent().getStringExtra("prodPrice");
        prodDescription = getIntent().getStringExtra("prodDescription");
        prodImage = getIntent().getStringExtra("prodImage");

        imageView = findViewById(R.id.productDetail_image);
        name = findViewById(R.id.productDetail_prodName);
        price = findViewById(R.id.productDetail_prodPrice);
        description = findViewById(R.id.productDetail_description);

        name.setText(prodName);
        price.setText(prodPrice);
        description.setText(prodDescription);
        Picasso.get().load(prodImage).into(imageView);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}