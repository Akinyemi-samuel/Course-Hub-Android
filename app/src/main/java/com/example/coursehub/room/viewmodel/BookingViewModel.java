package com.example.coursehub.room.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.coursehub.room.entities.Booking;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.entities.WishList;
import com.example.coursehub.room.repository.BookingRepository;
import com.example.coursehub.room.repository.WishListRepository;

import java.util.List;

public class BookingViewModel extends AndroidViewModel {

    BookingRepository repository;

    public BookingViewModel(@NonNull Application application) {
        super(application);
        repository = new BookingRepository(application);
    }

    public void deleteWishlistItem(Long userId, Long courseId) {
        repository.deleteWishlistItem(userId, courseId);
    }


    public void insert(Booking wishList, Context context, LifecycleOwner lifecycleOwner) {
        repository.insert(wishList, context, lifecycleOwner);
    }

    public LiveData<List<Booking>> getWishListByUserId(Long wishList){
        return repository.getWishListByUserId(wishList);
    }

    public LiveData<Boolean> isCourseInWishlistCheck(Booking booking) {
       return repository.isCourseInWishlistCheck(booking);
    }


    public LiveData<List<Course>> getCoursesInWishlist(Long userId) {
        return repository.getCoursesInWishlist(userId);
    }
}
