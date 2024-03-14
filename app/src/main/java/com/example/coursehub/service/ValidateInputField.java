package com.example.coursehub.service;

import android.widget.EditText;

import java.util.function.Function;

public class ValidateInputField implements Function<EditText, String> {

    @Override
    public String apply(EditText e) {
        return e.getText().toString().trim();
    }
}
