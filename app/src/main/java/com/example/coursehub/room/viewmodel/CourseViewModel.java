package com.example.coursehub.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.repository.CourseRepository;

import java.io.Closeable;
import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private CourseRepository repository;

    private LiveData<List<Course>> allCourse;

    MutableLiveData<Boolean> buttonClickedLiveData = new MutableLiveData<>();


    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new CourseRepository(application);
        allCourse = repository.getCourseByRandomLimit4();
        buttonClickedLiveData.setValue(false);
    }


    // Method to update the LiveData when the button is clicked
    public void buttonClickedTrue() {
        buttonClickedLiveData.setValue(true);
    }

    public void buttonClickedFalse() {
        buttonClickedLiveData.setValue(false);
    }

    // Getter method for observing the LiveData from the Activity
    public LiveData<Boolean> getButtonClickedLiveData() {
        return buttonClickedLiveData;
    }

    // Getter method for observing the LiveData from the Activit



    public void update(Course course){
        repository.update(course);
    }

    public void insertAll(List<Course> course){
        repository.insertAll(course);
    }

    public void delete(Course course){
        repository.delete(course);
    }

    public void deleteAllCourse(){
        repository.deleteAllCourse();
    }

    public LiveData<List<Course>> getCourseByRandomLimit4(){
        return allCourse;
    }

    public LiveData<List<Course>> getAllCourse(){
        return repository.getAllCourse();
    }

    public void refreshDatabase(){
        repository.refreshDatabase();
    }

    public LiveData<List<String>> getDistinctCategory(){
        return repository.getDistinctCategory();
    }

    public LiveData<List<Course>> getCourseByCategory(String category, int limit){
        return repository.getCourseByCategory(category, limit);
    }

    public LiveData<Course> getCourseById(Long category){
        return repository.getCourseById(category);
    }

    public LiveData<List<Course>> searchCourses(String query){
        return repository.searchCourses(query);
    }
}
