package com.example.coursehub.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.coursehub.room.entities.WishList;

import java.util.List;

@Dao
public interface WishListDao {

    @Insert
    void insert(WishList wishList);

    @Query("SELECT * FROM wishlist WHERE user_id = :id")
    LiveData<List<WishList>> getWishListByUserId(Long id);
}
