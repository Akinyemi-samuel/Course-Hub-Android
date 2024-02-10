package com.example.coursehub.adapter;

import android.os.AsyncTask;

import com.example.coursehub.dto.CourseCategory;

import java.util.ArrayList;
import java.util.List;

public class FetchCategoriesTask extends AsyncTask<Void, Void, List<CourseCategory>> {

    private CourseCategoriesAdapter categoryAdapter; // Assume you have a custom adapter for RecyclerView

    public FetchCategoriesTask(CourseCategoriesAdapter categoryAdapter) {
        this.categoryAdapter = categoryAdapter;
    }

    @Override
    protected List<CourseCategory> doInBackground(Void... voids) {
        // Simulate fetching categories from an API or database
        List<CourseCategory> courseCategories = new ArrayList<>();
        courseCategories.add(new CourseCategory("Category 1"));
        courseCategories.add(new CourseCategory("Category 2"));
        courseCategories.add(new CourseCategory("Category 3"));
        courseCategories.add(new CourseCategory("Category 4"));
        courseCategories.add(new CourseCategory("Category 5"));
        courseCategories.add(new CourseCategory("Category 6"));
        courseCategories.add(new CourseCategory("Category 7"));
        // Add more categories as needed
        return courseCategories;
    }

    @Override
    protected void onPostExecute(List<CourseCategory> categories) {
        super.onPostExecute(categories);
        // Update RecyclerView with fetched categories
        //categoryAdapter.setCategories(categories);
        categoryAdapter.notifyDataSetChanged();


    }
}
