package com.example.bancom_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductDetail extends AppCompatActivity {

    private String prodName, prodPrice, prodDescription,prodImage;
    private TextView name,price,description;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

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
}
