package com.example.coursehub.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursehub.activity.CourseDetail;
import com.example.coursehub.adapter.SearchCourseAdapter;
import com.example.coursehub.databinding.FragmentSearchBinding;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.viewmodel.CourseViewModel;
import com.example.coursehub.room.viewmodel.ReviewViewModel;

import java.util.List;


public class Search extends Fragment implements SearchCourseAdapter.ItemClickListener {
    FragmentSearchBinding binding;
    CourseViewModel courseViewModel;
    RecyclerView searchRecyclerView;

    SearchCourseAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        searchRecyclerView = binding.searchRecyclerview;
        SearchView searchView = binding.searchView;


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                courseViewModel.searchCourses(newText).observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
                    @Override
                    public void onChanged(List<Course> courses) {
                        adapter = new SearchCourseAdapter(Search.this, getContext(), new ViewModelProvider(Search.this).get(ReviewViewModel.class), getViewLifecycleOwner());
                        adapter.setCategories(courses);
                        searchRecyclerView.setAdapter(adapter);

                        binding.courseSize.setText("We found " + courses.size() + " courses available for you");

                    }
                });
                return true;
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onItemClick(Course course) {
        startActivity(new Intent(getContext(), CourseDetail.class).putExtra("courseId", course.getCourseId()));

    }
}