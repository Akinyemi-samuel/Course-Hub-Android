package com.example.coursehub.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.coursehub.dto.Review;
import com.example.coursehub.room.entities.Reviews;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert
    void insert(Reviews review);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReviews(List<Reviews> reviews);

    @Query("SELECT * FROM review WHERE course_id = :id")
    LiveData<List<Reviews>> getReviewById(Long id);


    @Query("SELECT COUNT(*) FROM review")
    int getNumberOfItems();


}
