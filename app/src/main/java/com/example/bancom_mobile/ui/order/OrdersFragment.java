package com.example.bancom_mobile.ui.order;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bancom_mobile.Model.Order;
import com.example.bancom_mobile.Model.Products;
import com.example.bancom_mobile.Prevalent.Prevalent;
import com.example.bancom_mobile.ProductDetail;
import com.example.bancom_mobile.R;
import com.example.bancom_mobile.ViewHolder.OrderViewHolder;
import com.example.bancom_mobile.ViewHolder.ProductViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import io.paperdb.Paper;

import static com.example.bancom_mobile.ui.home.HomeFragment.currencyFormat;

public class OrdersFragment extends Fragment {

    private OrdersViewModel mViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<Order, OrderViewHolder> adapter;

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.orders_fragment, container, false);

        final RecyclerView recyclerView = root.findViewById(R.id.recycle_view_orders);

        Query query = db.collection("Orders")
                .whereEqualTo("uid", Paper.book().read(Prevalent.UserPhoneKey));

        FirestoreRecyclerOptions<Order> options =
                new FirestoreRecyclerOptions.Builder<Order>()
                        .setQuery(query, new SnapshotParser<Order>() {
                            @NonNull
                            @Override
                            public Order parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                Order order = new Order();
                                order.setId(snapshot.getId());
                                order.setUid(snapshot.getString("uid"));
                                order.setName(snapshot.getString("name"));
                                order.setPhone(snapshot.getString("phone"));
                                order.setTotal(snapshot.getString("total"));
                                order.setDate(snapshot.getString("date"));
                                order.setTime(snapshot.getString("time"));
                                order.setCity(snapshot.getString("city"));
                                order.setState(snapshot.getString("state"));
                                order.setAddress(snapshot.getString("address"));
                                return order;
                            }
                        })
                        .build();

        adapter = new FirestoreRecyclerAdapter<Order, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Order model) {
                String name = model.getName();

                holder.txtOrderName.setText(model.getName());
                holder.txtOrderPhone.setText(model.getPhone());
                holder.txtOrderDateTime.setText(model.getDate()+ " " +model.getTime());
                holder.txtOrderAddressCity.setText(model.getAddress()+ " " +model.getCity());
                holder.txtOrderTotal.setText( currencyFormat(model.getTotal()) );


                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ProductDetail.class);
                        intent.putExtra("orderId",model.getId());
                        intent.putExtra("orderUid",model.getUid());
                        startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_items_layout,parent,false);
                OrderViewHolder holder = new OrderViewHolder(view);
                return holder;
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrdersViewModel.class);
        // TODO: Use the ViewModel
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
