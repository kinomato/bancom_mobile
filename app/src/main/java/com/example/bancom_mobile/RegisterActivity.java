package com.example.bancom_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

import io.opencensus.tags.Tag;
import io.paperdb.Paper;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputphoneNumber, InputPassword;
    private ProgressDialog loadingBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        CreateAccountButton = findViewById(R.id.register_btn);
        InputName = findViewById(R.id.register_username_input);
        InputphoneNumber = findViewById(R.id.register_phone_number_input);
        InputPassword = findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();
        Paper.init(this);
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }
    private void CreateAccount()
    {
        String name = InputName.getText().toString();
        String phone = InputphoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        // TextUtils cùng chức năng vói string.isempty nhưng dc khuyên dùng hơn
        // vì string.empty(null) sẽ trả về NullPointerException
        // còn TextUtils luôn trả về bool
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Registering..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(name,phone,password);
        }
    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password)
    {

        DocumentReference docRef = db.collection("Users").document(phone);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().exists())
                    {
                        Toast.makeText(RegisterActivity.this, "This" + phone + "already exists", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Please try using another phone number", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Map<String, Object> userdataMap = new HashMap<>();
                        userdataMap.put("name",name);
                        userdataMap.put("phone",phone);
                        userdataMap.put("password", password);
                        db.collection("Users").document(phone).set(userdataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(RegisterActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
                                            loadingBar.setTitle("Logging in");
                                            loadingBar.setMessage("Please wait");
                                            AllowAccess(phone, password);
                                            /*loadingBar.dismiss();

                                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                            startActivity(intent);*/
                                        }
                                        else
                                        {
                                            Toast.makeText(RegisterActivity.this, "Failed, Please try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    System.out.println( "get failed with "+ task.getException());
                }

            }
        });
        docRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Network error, please try another time", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        });
    }
    private void AllowAccess(final String phone, final String password) {

        Paper.book().write(Prevalent.UserPhoneKey,phone);
        Paper.book().write(Prevalent.UserPasswordKey,password);
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

                            Toast.makeText(RegisterActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Wrong userPasswordKey", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegisterActivity.this, "Account with" + phone + "does not exist", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegisterActivity.this, "Network error, please try another time", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        });
    }
}
