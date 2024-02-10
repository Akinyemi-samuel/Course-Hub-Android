package com.example.coursehub.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "user_id")
    private Long userId;

    private String socialId;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    private String email;

    @ColumnInfo(name = "is_email_valid")
    private boolean isEmailValid;

    private boolean fingerprint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailValid() {
        return isEmailValid;
    }

    public void setEmailValid(boolean emailValid) {
        isEmailValid = emailValid;
    }

    public boolean isFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(boolean fingerprint) {
        this.fingerprint = fingerprint;
    }

    public User(Long id, Long userId, String socialId, String firstName, String lastName, String email, boolean isEmailValid, boolean fingerprint) {
        this.id = id;
        this.userId = userId;
        this.socialId = socialId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isEmailValid = isEmailValid;
        this.fingerprint = fingerprint;
    }
}
