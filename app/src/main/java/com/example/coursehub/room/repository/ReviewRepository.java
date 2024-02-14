package com.example.coursehub.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.android.volley.toolbox.Volley;
import com.example.coursehub.room.dao.CourseDao;
import com.example.coursehub.room.dao.ReviewDao;
import com.example.coursehub.room.database.CourseDatabase;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.entities.Reviews;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReviewRepository {

    private ReviewDao reviewDao;

    public ReviewRepository(Application application) {
        CourseDatabase database = CourseDatabase.getInstance(application);
        reviewDao = database.reviewDao();
    }


    public LiveData<List<Reviews>> getReviewsById(Long id) {
        return reviewDao.getReviewById(id);
    }



}
