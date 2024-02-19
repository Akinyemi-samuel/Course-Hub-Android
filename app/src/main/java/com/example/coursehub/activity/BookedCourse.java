package com.example.coursehub.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.coursehub.R;
import com.example.coursehub.adapter.WishListAdapter;
import com.example.coursehub.databinding.ActivityBookedCourseBinding;
import com.example.coursehub.databinding.FragmentWishListBinding;
import com.example.coursehub.fragment.WishList;
import com.example.coursehub.room.entities.Booking;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.viewmodel.BookingViewModel;
import com.example.coursehub.room.viewmodel.CourseViewModel;
import com.example.coursehub.room.viewmodel.ReviewViewModel;
import com.example.coursehub.room.viewmodel.WishListViewModel;

import java.util.List;

public class BookedCourse extends AppCompatActivity implements WishListAdapter.ItemClickListener {
    BookingViewModel bookingViewModel;
    CourseViewModel courseViewModel;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    WishListAdapter randomCourseAdapter;

    String userId;

    ActivityBookedCourseBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookedCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userId = sharedPreferences.getString("id", "0");

        randomCourseAdapter = new WishListAdapter(BookedCourse.this, getApplicationContext(), new ViewModelProvider(BookedCourse.this).get(ReviewViewModel.class), this);


        bookingViewModel.getCoursesInWishlist(Long.parseLong(userId)).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                if (!courses.isEmpty()) {
                    binding.emptyBox.setVisibility(View.GONE);
                }
                randomCourseAdapter.setCategories(courses);
                binding.bookedRecyclerview.setAdapter(randomCourseAdapter);
                binding.bookedRecyclerview.setHasFixedSize(true);
            }
        });


    }

    @Override
    public void onItemClick(Course course) {
        startActivity(new Intent(getApplicationContext(), CourseDetail.class).putExtra("courseId", course.getCourseId()));
    }

    @Override
    public void onItemClick2(Course course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this course?");
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                bookingViewModel.deleteWishlistItem(Long.parseLong(userId), course.getCourseId());
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

    public void goBack(View view) {
        onBackPressed();
    }
}