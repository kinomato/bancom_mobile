package com.example.bancom_mobile.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancom_mobile.HomeActivity;
import com.example.bancom_mobile.Model.Products;
import com.example.bancom_mobile.ProductDetail;
import com.example.bancom_mobile.R;
import com.example.bancom_mobile.ViewHolder.ProductViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<Products, ProductViewHolder> adapter;
/*
    private RecyclerView recyclerView;
*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);*/
        final RecyclerView recyclerView = root.findViewById(R.id.recycle_view_products);
        /*homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/



        Query query = db.collection("Products")
                .orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Products> options =
                new FirestoreRecyclerOptions.Builder<Products>()
                        .setQuery(query, new SnapshotParser<Products>() {
                            @NonNull
                            @Override
                            public Products parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                Products products = new Products();
                                products.setId(snapshot.getId());
                                products.setName(snapshot.getString("name"));
                                products.setDescription(snapshot.get("description").toString());
                                products.setPrice(snapshot.getString("price"));
                                products.setImage(snapshot.getString("image"));
                                return products;
                            }
                        })
                        .build();


        adapter =
                new FirestoreRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {

                        holder.txtProductName.setText(model.getName());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText( currencyFormat(model.getPrice()) );
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ProductDetail.class);
                                intent.putExtra("prodId",model.getId());
                                intent.putExtra("prodName",model.getName());
                                intent.putExtra("prodPrice",model.getPrice());
                                intent.putExtra("prodDescription",model.getDescription());
                                intent.putExtra("prodImage",model.getImage());

                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.product_items_layout,parent,false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.stopListening();
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(Double.parseDouble(amount));
    }
}