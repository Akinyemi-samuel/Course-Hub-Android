package com.example.coursehub.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.coursehub.room.entities.Course;
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

    @Query("SELECT * FROM course WHERE course_id IN (SELECT course_id FROM wishlist WHERE user_id = :userId)")
    LiveData<List<Course>> getCoursesInWishlist(Long userId);

    @Query("DELETE FROM wishlist WHERE user_id = :userId AND course_id = :courseId")
    void deleteWishlistItem(Long userId, Long courseId);

}
