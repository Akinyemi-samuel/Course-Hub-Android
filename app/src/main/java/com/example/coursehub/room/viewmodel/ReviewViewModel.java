package com.example.coursehub.room.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.entities.Reviews;
import com.example.coursehub.room.entities.WishList;
import com.example.coursehub.room.repository.ReviewRepository;
import com.example.coursehub.room.repository.WishListRepository;

import java.util.List;

public class ReviewViewModel extends AndroidViewModel {

    ReviewRepository repository;

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        repository = new ReviewRepository(application);
    }


    public LiveData<List<Reviews>> getReviewsById(Long id) {
        return repository.getReviewsById(id);
    }

}
