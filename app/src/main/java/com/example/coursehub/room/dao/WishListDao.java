package com.example.coursehub.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.coursehub.room.entities.WishList;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Dao
public interface WishListDao {

    @Insert
    void insert(WishList wishList);

    @Query("SELECT * FROM wishlist WHERE user_id = :id")
    LiveData<List<WishList>> getWishListByUserId(Long id);

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE course_id = :courseId AND user_id = :userId LIMIT 1)")
    List<Boolean> isCourseInWishlist(String courseId, String userId);

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE course_id = :courseId AND user_id = :userId LIMIT 1)")
    LiveData<Boolean> isCourseInWishlistLiveData(String courseId, String userId);
}
