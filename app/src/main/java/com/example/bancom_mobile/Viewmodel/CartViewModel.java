package com.example.bancom_mobile.Viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CartViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Integer> mTotalPrice=new MutableLiveData<>();

    public CartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Integer> getmTotalPrice() {
        return mTotalPrice;
    }

    public void setmTotalPrice(Integer mPrice) {
        this.mTotalPrice.setValue(mPrice);
    }
    public void summTotalPrice(Integer mPrice) {
        int total = mTotalPrice.getValue();
        total = total + mPrice;
        this.mTotalPrice.setValue(total);
    }
}
