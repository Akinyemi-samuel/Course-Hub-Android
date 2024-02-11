package com.example.coursehub.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "booking")
public class Booking {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long user;

    private Long course;

    @ColumnInfo(name = "booking_date_time")
    private Long date;

    @ColumnInfo(name = "total_cost")
    private Double totalCost;


    public Booking(Long user, Long course, Long date, Double totalCost) {
        this.user = user;
        this.course = course;
        this.date = date;
        this.totalCost = totalCost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getCourse() {
        return course;
    }

    public void setCourse(Long course) {
        this.course = course;
    }


    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
}
