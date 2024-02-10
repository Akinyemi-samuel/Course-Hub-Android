package com.example.coursehub.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.coursehub.room.entities.User;
import com.example.coursehub.room.repository.CourseRepository;
import com.example.coursehub.room.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void update(User user) {
        repository.update(user);
    }

    public void insert(User user) {
        repository.insert(user);
    }
}
