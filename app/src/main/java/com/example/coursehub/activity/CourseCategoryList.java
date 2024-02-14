package com.example.coursehub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursehub.MainActivity;
import com.example.coursehub.R;
import com.example.coursehub.adapter.CourseAdapter;
import com.example.coursehub.databinding.ActivityCourseCategoryListBinding;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.viewmodel.CourseViewModel;
import com.example.coursehub.room.viewmodel.ReviewViewModel;

import java.util.List;

public class CourseCategoryList extends AppCompatActivity implements CourseAdapter.ItemClickListener {

    ActivityCourseCategoryListBinding binding;
    CourseViewModel courseViewModel;
    RecyclerView categoryRecyclerView;
    CourseAdapter courseAdapter;
    String courseCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseCategoryListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        courseAdapter = new CourseAdapter(CourseCategoryList.this, getApplicationContext(), new ViewModelProvider(this).get(ReviewViewModel.class), this);

        courseCategory = getIntent().getStringExtra("courseCategory");

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        categoryRecyclerView = findViewById(R.id.category_recyclerview);

        courseViewModel.getButtonClickedLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    categoryRecyclerView.setLayoutManager(gridLayoutManager);
                    binding.grid.setBackgroundTintList(getResources().getColorStateList(R.color.brown));
                    binding.column.setBackgroundTintList(getResources().getColorStateList(R.color.text_color));
                    categoryRecyclerView.setAdapter(courseAdapter);
                    return;
                } else {
                    categoryRecyclerView.setLayoutManager(linearLayoutManager);
                    binding.column.setBackgroundTintList(getResources().getColorStateList(R.color.brown));
                    binding.grid.setBackgroundTintList(getResources().getColorStateList(R.color.text_color));
                    categoryRecyclerView.setAdapter(courseAdapter);
                    return;
                }

            }
        });

        courseViewModel.getCourseByCategory(courseCategory, 50).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {

                courseAdapter.setCategories(courses);
            }
        });

    }

    @Override
    public void onItemClick(Course course) {

        startActivity(new Intent(getApplicationContext(), CourseDetail.class).putExtra("courseId", course.getCourseId()));
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void gridCourse(View view) {
        courseViewModel.buttonClickedFalse();
    }

    public void ColumnCourse(View view) {
        courseViewModel.buttonClickedTrue();
    }

    public void openSearch(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("openSearchFragment", true);
        startActivity(intent);
    }
}