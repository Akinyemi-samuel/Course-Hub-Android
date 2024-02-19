package com.example.coursehub.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.coursehub.room.entities.Booking;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.entities.WishList;

import java.util.List;

@Dao
public interface BookingDao {

    @Insert
    void insert(Booking booking);

    @Query("DELETE FROM booking WHERE user = :userId AND course = :courseId")
    void deleteWishlistItem(Long userId, Long courseId);

    @Query("SELECT * FROM course WHERE course_id IN (SELECT course FROM booking WHERE user = :userId)")
    LiveData<List<Course>> getCoursesInWishlist(Long userId);

    @Query("SELECT EXISTS(SELECT 1 FROM booking WHERE course = :courseId AND user = :userId LIMIT 1)")
    LiveData<Boolean> isCourseInWishlistLiveData(String courseId, String userId);

    @Query("SELECT EXISTS(SELECT 1 FROM booking WHERE course = :courseId AND user = :userId LIMIT 1)")
    List<Boolean> isCourseInWishlist(String courseId, String userId);

    @Query("SELECT * FROM booking WHERE user = :id")
    LiveData<List<Booking>> getWishListByUserId(Long id);

}
