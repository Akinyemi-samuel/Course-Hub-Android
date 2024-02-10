package com.example.coursehub.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.entities.User;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Query("DELETE FROM user")
    void deleteAllCourse();

    @Query("SELECT * FROM user WHERE user_id = :id")
    LiveData<User> getCourseById(Long id);
}
