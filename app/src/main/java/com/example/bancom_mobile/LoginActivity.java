package com.example.bancom_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bancom_mobile.Model.Users;
import com.example.bancom_mobile.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button LoginButton;
    private EditText InputphoneNumber, InputPassword;
    private ProgressDialog loadingBar;


    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton = findViewById(R.id.login_btn);
        InputphoneNumber = findViewById(R.id.login_phone_number_input);
        InputPassword = findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login()
    {
        String phone = InputphoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        // TextUtils cùng chức năng vói string.isempty nhưng dc khuyên dùng hơn
        // vì string.empty(null) sẽ trả về NullPointerException
        // còn TextUtils luôn trả về bool

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login");
            loadingBar.setMessage("Logging in");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccess(phone,password);
        }
    }

    private void AllowAccess( final String phone, final String password)
    {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }*/
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
                            // lưu dữ liệu người dùng hiện tại
                            Users userData = new Users();
                            userData.setName(document.getData().get("name").toString());
                            userData.setPhone(document.getData().get("phone").toString());
                            userData.setPassword(document.getData().get("password").toString());
                            Prevalent.currrentOnlineUser = userData;

                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            // dừng luôn activity main và login để ngăn người dùng quay lại
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            // dừng activity hiện tại
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LoginActivity.this, "Account with" + phone + "does not exist", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, "Network error, please try another time", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        });
    }
}
