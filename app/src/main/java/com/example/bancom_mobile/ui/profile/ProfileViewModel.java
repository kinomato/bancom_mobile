package com.example.bancom_mobile.ui.profile;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<String> mName,mPassword;

    public ProfileViewModel() {

        mName = new MutableLiveData<>();
        mPassword = new MutableLiveData<>();
        mName.setValue("This is user name");
        mPassword.setValue("This is user password");
    }
    public LiveData<String> getmName() {
        return mName;
    }
    public LiveData<String> getmPassword() {
        return mPassword;
    }
    public void setmName(String name) {
        mName.setValue(name);
    }
    public void setmPassword(String password) {
        mPassword.setValue(password);
    }
    public void LoadUserData(String phone) {
        db.collection("Users").document(phone)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        setmName(documentSnapshot.getString("name"));
                        setmPassword(documentSnapshot.getString("password"));
                    }
                });
    }
}
