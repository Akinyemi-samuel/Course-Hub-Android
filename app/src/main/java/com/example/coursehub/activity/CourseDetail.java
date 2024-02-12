package com.example.coursehub.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.coursehub.R;
import com.example.coursehub.adapter.CourseAdapter;
import com.example.coursehub.databinding.ActivityCourseDetailBinding;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.viewmodel.CourseViewModel;
import com.example.coursehub.service.NetworkUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CourseDetail extends AppCompatActivity {

    ActivityCourseDetailBinding binding;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String token;
    CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        token = sharedPreferences.getString("token", null);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        Long courseId = getIntent().getLongExtra("courseId", 0);

        courseViewModel.getCourseById(courseId).observe(this, new Observer<Course>() {
            @Override
            public void onChanged(Course course) {

                Glide.with(getApplicationContext()).load(course.getImageUrl())
                        .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(10)))
                        .into(binding.courseImg);
                binding.description.setText(course.getDescription());
                binding.instructor.setText(course.getInstructor());
                binding.duration.setText(String.format("%s Weeks", course.getDuration()));
                Double price = course.getPrice();
                String COUNTRY = "US";
                String LANGUAGE = "en";
                String formattedPrice = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(price);
                binding.price.setText(formattedPrice);
                binding.nameOfCourse.setText(course.getName());
                binding.location.setText(course.getLocation());
            }
        });


        binding.wishlist.setOnClickListener(n -> {

            boolean isConnected = NetworkUtils.isNetworkAvailable(getApplicationContext());
            if (! isConnected) {
                Toast.makeText(this, "No internet Connection available", Toast.LENGTH_SHORT).show();
                return;
            }

            if (token == null || token.isEmpty()){
                signInDialog();
                return;
            }
        });


    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void bookTheBtn(View view) {
        if (token == null || token.isEmpty()){
            signInDialog();
        }
    }



    public void signInDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" Please sign in to continue...");
        builder.setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }

}