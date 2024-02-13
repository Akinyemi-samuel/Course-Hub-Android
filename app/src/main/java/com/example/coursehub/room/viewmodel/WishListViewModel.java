package com.example.coursehub.room.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.coursehub.room.entities.User;
import com.example.coursehub.room.entities.WishList;
import com.example.coursehub.room.repository.UserRepository;
import com.example.coursehub.room.repository.WishListRepository;

public class WishListViewModel extends AndroidViewModel {

    WishListRepository repository;

    public WishListViewModel(@NonNull Application application) {
        super(application);
        repository = new WishListRepository(application);
    }


    public void insert(WishList wishList, Context context, LifecycleOwner lifecycleOwner) {
        repository.insert(wishList, context, lifecycleOwner);
    }


    public LiveData<Boolean> isCourseInWishlistCheck(WishList wishList) {
       return repository.isCourseInWishlistCheck(wishList);
    }
}
