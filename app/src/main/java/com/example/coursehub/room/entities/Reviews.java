package com.example.coursehub.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "review")
public class Reviews {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "course_id")
    private Long course;

    @ColumnInfo(name = "user_id")
    private Long user;

    private String comment;

    private int rating;


    public Reviews(Long course, Long user, String comment, int rating) {
        this.course = course;
        this.user = user;
        this.comment = comment;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourse() {
        return course;
    }

    public void setCourse(Long course) {
        this.course = course;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Reviews{" +
                "id=" + id +
                ", course=" + course +
                ", user=" + user +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                '}';
    }
}
