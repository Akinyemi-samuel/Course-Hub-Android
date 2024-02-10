package com.example.coursehub.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coursehub.MainActivity;
import com.example.coursehub.databinding.ActivityWellcomeBinding;

public class Wellcome extends AppCompatActivity {

    ActivityWellcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWellcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startApplication.setOnClickListener(view ->{
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        binding.startApplication2.setOnClickListener(view ->{
            startActivity(new Intent(getApplicationContext(), Login.class));
        });
    }
}