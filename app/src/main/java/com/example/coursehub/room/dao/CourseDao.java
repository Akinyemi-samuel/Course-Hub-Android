package com.example.coursehub.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

import com.example.coursehub.room.entities.Course;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("DELETE FROM course")
    void deleteAllCourse();

    @Query("SELECT * FROM course ORDER BY RANDOM() LIMIT 4")
    LiveData<List<Course>> getCourseByRandomLimit4();

    @Query("SELECT * FROM course")
    LiveData<List<Course>> getAllCourses();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourses(List<Course> courses);

    @Query("SELECT COUNT(*) FROM course")
    int getNumberOfItems();

    @Query("SELECT DISTINCT category FROM course")
    LiveData<List<String>> getDistinctCategory();

    @Query("SELECT * FROM course WHERE category = :category LIMIT :limit")
    LiveData<List<Course>> getCourseByCategory(String category, int limit);

    @Query("SELECT * FROM course WHERE course_id = :id")
    LiveData<Course> getCourseById(Long id);

    @Query("SELECT * FROM course WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'")
    LiveData<List<Course>> searchCourses(String query);
}
