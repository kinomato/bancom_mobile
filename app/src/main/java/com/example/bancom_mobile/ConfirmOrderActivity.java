package com.example.bancom_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bancom_mobile.Model.CartItem;
import com.example.bancom_mobile.Prevalent.Prevalent;
import com.example.bancom_mobile.Viewmodel.CartViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

import static com.example.bancom_mobile.ui.home.HomeFragment.currencyFormat;

public class ConfirmOrderActivity extends AppCompatActivity {

    private CartViewModel cartViewModel;
    private TextView money;
    private EditText eName, ePhone, eAddress, eCity;
    private Button confirmBtn;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        db = FirebaseFirestore.getInstance();
        eName = findViewById(R.id.confirm_name);
        ePhone = findViewById(R.id.confirm_phone);
        eAddress = findViewById(R.id.confirm_address);
        eCity = findViewById(R.id.confirm_city);
        confirmBtn = findViewById(R.id.confirm_checkout_btn);
        money = findViewById(R.id.confirm_total_value);
        money.setText(currencyFormat(getIntent().getStringExtra("price")));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("Confirm order");

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check() {
        if (TextUtils.isEmpty(eName.getText().toString())
                || TextUtils.isEmpty(eName.getText().toString())
                || TextUtils.isEmpty(eName.getText().toString())
                || TextUtils.isEmpty(eName.getText().toString())
        ) {
            Toast.makeText(this, "Please input all the field!", Toast.LENGTH_SHORT).show();
            return;
        }
        confirm();
    }

    private void confirm() {
        WriteBatch batch = db.batch();
        final String saveCurrentDate, saveCurrentTime;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        String userPhone = Paper.book().read(Prevalent.UserPhoneKey);

        final Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("uid", userPhone);
        orderMap.put("name", eName.getText().toString());
        orderMap.put("phone", ePhone.getText().toString());
        orderMap.put("address", eAddress.getText().toString());
        orderMap.put("city", eCity.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("total", getIntent().getStringExtra("price"));
        orderMap.put("state", "not shipped");


        final DocumentReference docRef = db.collection("Orders").document();

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                docRef.set(orderMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                }
                            }
                        });
            }
        });
        batch.set(docRef, orderMap);
        ArrayList<CartItem> cartItems = Paper.book().read("cart");
        for (int i = 0; i < cartItems.size(); i++) {

            CartItem item = cartItems.get(i);
            final DocumentReference cartItemRef = db.collection("Carts").document(userPhone)
                    .collection(docRef.getId()).document();

            final Map<String, Object> cartItemMap = new HashMap<>();
            cartItemMap.put("name", item.getName());
            cartItemMap.put("description", item.getDescription());
            cartItemMap.put("price", item.getPrice());
            cartItemMap.put("quantity", item.getQuantity());
            cartItemMap.put("image", item.getImage());

            batch.set(cartItemRef,cartItemMap);
        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ConfirmOrderActivity.this, "set Order succeed", Toast.LENGTH_SHORT).show();
                Paper.book().write("cart",new ArrayList<CartItem>());
                Prevalent.cartTotalPrice=0;
                Intent intent = new Intent(ConfirmOrderActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
