package com.example.bancom_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bancom_mobile.Model.Users;
import com.example.bancom_mobile.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button johnNowButton, loginButton;
    private ProgressDialog loadingBar;
    private FirebaseFirestore db;
    private String UserPhoneKey;
    String UserPasswordKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingBar = new ProgressDialog(this);
        johnNowButton = (Button) findViewById(R.id.main_john_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        db = FirebaseFirestore.getInstance();
        Paper.init(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        johnNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPasswordKey != "" && UserPasswordKey != "")
        {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                loadingBar.setTitle("Already logged in");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                AllowAccess(UserPhoneKey, UserPasswordKey);


            }
        }
    }

    private void AllowAccess(final String phone, final String password) {


        DocumentReference docRef = db.collection("Users").document(phone);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().exists())
                    {
                        loadingBar.dismiss();

                        DocumentSnapshot document = task.getResult();

                        if(document.getData().get("password").equals(password))
                        {
                            Users userData = new Users();
                            userData.setName(document.getData().get("name").toString());
                            userData.setPhone(document.getData().get("phone").toString());
                            userData.setPassword(document.getData().get("password").toString());
                            Prevalent.currrentOnlineUser = userData;

                            Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Wrong userPasswordKey", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                        /*if (document.exists()) {
                            Log.d("Testing", "testing");
                            Log.d("Login", "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d("Login fail", "No such document");
                        }*/
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Account with" + phone + "does not exist", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                } else {
                    System.out.println( "get failed with "+ task.getException());
                }

            }
        });
        docRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Network error, please try another time", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        });
    }
}
