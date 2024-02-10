package com.example.coursehub.dto;

public class Review {

    private Long id;

    private String comment;

    private int rating;

    public int getRating() {
        return rating;
    }

    public Long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public Review(String comment, int rating) {
        this.comment = comment;
        this.rating = rating;
    }
}
