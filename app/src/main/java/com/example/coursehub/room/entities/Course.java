package com.example.coursehub.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.coursehub.dto.Review;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Entity(tableName = "course")
public class Course {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "course_id")
    private Long courseId;
    private String name;
    private String instructor;
    private String duration;
    private String date;
    private String time;
    private String location;
    private double price;
    private String description;
    private String category;
    @ColumnInfo(name = "image_url")
    private String imageUrl;

    public Course(Long courseId, String name, String instructor, String duration, String date, String time, String location, double price, String description, String category, String imageUrl) {
        this.courseId = courseId;
        this.name = name;
        this.instructor = instructor;
        this.duration = duration;
        this.date = date;
        this.time = time;
        this.location = location;
        this.price = price;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
