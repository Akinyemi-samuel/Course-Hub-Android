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

import com.example.coursehub.R;
import com.example.coursehub.adapter.CourseAdapter;
import com.example.coursehub.databinding.ActivityCourseCategoryListBinding;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.viewmodel.CourseViewModel;

import java.util.List;

public class CourseCategoryList extends AppCompatActivity implements CourseAdapter.ItemClickListener {

    ActivityCourseCategoryListBinding binding;
    CourseViewModel courseViewModel;
    RecyclerView categoryRecyclerView;
    CourseAdapter courseAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseCategoryListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String courseCategory = getIntent().getStringExtra("courseCategory");

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        categoryRecyclerView = findViewById(R.id.category_recyclerview);

        courseViewModel.getCourseByCategory(courseCategory, 50).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                courseAdapter = new CourseAdapter(CourseCategoryList.this, getApplicationContext());
                courseAdapter.setCategories(courses);
                categoryRecyclerView.setAdapter(courseAdapter);

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        categoryRecyclerView.setLayoutManager(gridLayoutManager);
        binding.grid.setBackgroundTintList(getResources().getColorStateList(R.color.brown));
        binding.column.setBackgroundTintList(getResources().getColorStateList(R.color.text_color));
    }

    public void ColumnCourse(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.column.setBackgroundTintList(getResources().getColorStateList(R.color.brown));
        binding.grid.setBackgroundTintList(getResources().getColorStateList(R.color.text_color));
    }
}