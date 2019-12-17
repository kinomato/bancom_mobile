package com.example.bancom_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.bancom_mobile.Model.CartItem;
import com.example.bancom_mobile.Model.Order;
import com.example.bancom_mobile.Prevalent.Prevalent;
import com.example.bancom_mobile.ViewHolder.OrderProductViewHolder;
import com.example.bancom_mobile.ViewHolder.OrderViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

import static com.example.bancom_mobile.ui.order.OrdersFragment.currencyFormat;

public class OrderProductActivity extends AppCompatActivity {

    private String orderId,orderUid;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<CartItem, OrderProductViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        orderId = getIntent().getStringExtra("orderId");
        orderUid =getIntent().getStringExtra("orderUid");

        final RecyclerView recyclerView = findViewById(R.id.recycle_view_order_products);

        Query query = db.collection("Carts").document(orderUid).collection(orderId);


        FirestoreRecyclerOptions<CartItem> options =
                new FirestoreRecyclerOptions.Builder<CartItem>()
                        .setQuery(query, new SnapshotParser<CartItem>() {
                            @NonNull
                            @Override
                            public CartItem parseSnapshot(@NonNull DocumentSnapshot snapshot) {

                                CartItem cartItem = new CartItem();
                                cartItem.setName(snapshot.getString("name"));
                                cartItem.setPrice(snapshot.getString("price"));
                                cartItem.setDescription(snapshot.getString("description"));
                                cartItem.setQuantity(Integer.valueOf(snapshot.get("quantity").toString()));
                                cartItem.setImage(snapshot.getString("image"));
                                return cartItem;
                            }
                        })
                        .build();

        adapter = new FirestoreRecyclerAdapter<CartItem, OrderProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position, @NonNull final CartItem model) {


                holder.txtProductName.setText(model.getName());
                holder.txtProductPrice.setText(currencyFormat(model.getPrice()));
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductQuantity.setText(model.getQuantity().toString());
                Picasso.get().load(model.getImage()).into(holder.imageView);


            }
            @NonNull
            @Override
            public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_product_items_layout,parent,false);
                OrderProductViewHolder holder = new OrderProductViewHolder(view);
                return holder;
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("Order Product");
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }
}
