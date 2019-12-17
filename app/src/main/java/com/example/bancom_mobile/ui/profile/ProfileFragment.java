package com.example.bancom_mobile.ui.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bancom_mobile.Prevalent.Prevalent;
import com.example.bancom_mobile.R;
import com.example.bancom_mobile.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    private EditText  ePhone,eName,ePassword;
    private Button savebtn;

    private String name,phone,password;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        phone=Prevalent.currrentOnlineUser.getPhone();
        eName = root.findViewById(R.id.edit_profile_name);
        ePassword = root.findViewById(R.id.edit_profile_password);
        ePhone = root.findViewById(R.id.edit_profile_phone);
        ePhone.setText(phone);
        ePhone.setEnabled(false);
        savebtn = root.findViewById(R.id.btn_profile_save);


        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        mViewModel.LoadUserData(Prevalent.currrentOnlineUser.getPhone());
        mViewModel.getmName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                eName.setText(s);
            }
        });
        mViewModel.getmPassword().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ePassword.setText(s);
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        return root;
    }

    private void check() {
        name = eName.getText().toString();
        password = ePassword.getText().toString();
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password)){
            Toast.makeText(getContext(), "Please input all the field", Toast.LENGTH_SHORT).show();
            return;
        }
        save();

    }

    private void save() {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("name",name);
        userdataMap.put("password", password);
        db.collection("Users").document(phone).set(userdataMap, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getContext(), "Succeed", Toast.LENGTH_SHORT).show();
                           /* loadingBar.setTitle("Logging in");
                            loadingBar.setMessage("Please wait");*/
                            UpdateData(name, password);
                                            /*loadingBar.dismiss();

                                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                            startActivity(intent);*/
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Failed, Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void UpdateData(String name, String password) {
        Prevalent.currrentOnlineUser.setName(name);
        Prevalent.currrentOnlineUser.setPassword(password);
        Paper.book().write(Prevalent.UserPasswordKey,password);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

}
