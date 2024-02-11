package com.example.coursehub.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.coursehub.room.entities.Booking;

@Dao
public interface BookingDao {

    @Insert
    void insert(Booking booking);
}
