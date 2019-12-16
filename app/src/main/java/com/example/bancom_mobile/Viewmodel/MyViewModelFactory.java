package com.example.bancom_mobile.Viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class MyViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    public MyViewModelFactory(@NonNull Application application) {
        super(application);
    }
}
