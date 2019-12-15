package com.example.bancom_mobile.Prevalent;

import com.example.bancom_mobile.Model.CartItem;
import com.example.bancom_mobile.Model.Users;

import java.util.ArrayList;

public class Prevalent {
    public static Users currrentOnlineUser;

    public static final String UserPhoneKey = "UserPhone";
    public static final String UserPasswordKey = "UserPasswordKey";

    public static final ArrayList<CartItem> cart = new ArrayList<CartItem>();

    public static Integer cartTotalPrice = 0;
}
