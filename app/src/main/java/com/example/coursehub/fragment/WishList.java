package com.example.coursehub.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursehub.activity.CourseDetail;
import com.example.coursehub.adapter.RandomCourseAdapter;
import com.example.coursehub.adapter.SearchCourseAdapter;
import com.example.coursehub.adapter.WishListAdapter;
import com.example.coursehub.databinding.FragmentWishListBinding;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.viewmodel.CourseViewModel;
import com.example.coursehub.room.viewmodel.ReviewViewModel;
import com.example.coursehub.room.viewmodel.WishListViewModel;

import java.util.List;

public class WishList extends Fragment implements WishListAdapter.ItemClickListener {

    FragmentWishListBinding binding;
    WishListViewModel wishListViewModel;
    CourseViewModel courseViewModel;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    WishListAdapter randomCourseAdapter;

    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWishListBinding.inflate(inflater);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        wishListViewModel = new ViewModelProvider(requireActivity()).get(WishListViewModel.class);

        sharedPreferences = requireActivity().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userId = sharedPreferences.getString("id", "0");

        randomCourseAdapter = new WishListAdapter(WishList.this, getContext(), new ViewModelProvider(WishList.this).get(ReviewViewModel.class), getViewLifecycleOwner());


        wishListViewModel.getCoursesInWishlist(Long.parseLong(userId)).observe(getActivity(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                if (!courses.isEmpty()) {
                    binding.emptyBox.setVisibility(View.GONE);
                }
                randomCourseAdapter.setCategories(courses);
                binding.wishlistRecyclerview.setAdapter(randomCourseAdapter);
                binding.wishlistRecyclerview.setHasFixedSize(true);
            }
        });



        return binding.getRoot();
    }

    @Override
    public void onItemClick(Course course) {
        startActivity(new Intent(getContext(), CourseDetail.class).putExtra("courseId", course.getCourseId()));
    }

    @Override
    public void onItemClick2(Course course) {
        wishListViewModel.deleteWishlistItem(Long.parseLong(userId), course.getCourseId());
        System.out.println(course.getCourseId());
    }
}