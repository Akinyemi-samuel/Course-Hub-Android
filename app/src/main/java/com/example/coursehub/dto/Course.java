package com.example.coursehub.dto;

import java.util.ArrayList;
import java.util.List;

public class Course {

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
    private String imageUrl;

    List<Review> reviews = new ArrayList<>();

    public List<Review> getReviews() {
        return reviews;
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
