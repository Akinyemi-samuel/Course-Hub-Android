package com.example.coursehub.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

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


    public void insert(WishList wishList) {
        repository.insert(wishList);
    }
}
