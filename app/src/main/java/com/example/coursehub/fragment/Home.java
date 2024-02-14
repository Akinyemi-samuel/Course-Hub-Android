package com.example.coursehub.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.coursehub.R;
import com.example.coursehub.activity.CourseCategoryList;
import com.example.coursehub.activity.CourseDetail;
import com.example.coursehub.activity.Notification;
import com.example.coursehub.adapter.CourseAdapter;
import com.example.coursehub.adapter.CourseCategoriesAdapter;
import com.example.coursehub.adapter.RandomCourseAdapter;
import com.example.coursehub.databinding.FragmentHomeBinding;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.viewmodel.CourseViewModel;
import com.example.coursehub.room.viewmodel.ReviewViewModel;
import com.example.coursehub.service.Pair;
import com.example.coursehub.service.UserService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Home extends Fragment implements CourseCategoriesAdapter.ItemClickListener, RandomCourseAdapter.ItemClickListener, CourseAdapter.ItemClickListener {
    RecyclerView categoryRecyclerView, randomCourseRecyclerView, data_science_category_recyclerview, programming_category_recyclerview;
    CourseViewModel courseViewModel;

    FragmentHomeBinding binding;
    CourseCategoriesAdapter adapter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ShimmerFrameLayout shimmerFrameLayout, shimmerFrameLayoutCategory;

    String token;
    RandomCourseAdapter randomCourseAdapter;

    CourseAdapter courseAdapter;
    ImageFilterView gotNotification;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        View view = binding.getRoot();
        // Inflate the layout for this fragment

        gotNotification = view.findViewById(R.id.notification);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayoutCategory = view.findViewById(R.id.shimmer_view_container_category);
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayoutCategory.setVisibility(View.VISIBLE);
        shimmerFrameLayoutCategory.startShimmer();

        sharedPreferences = requireActivity().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", null);


        // lIST OF CATEGORIES
        categoryRecyclerView = view.findViewById(R.id.recylerCategory);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getActivity());
        categoryRecyclerView.setLayoutManager(flexboxLayoutManager);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        randomCourseRecyclerView = view.findViewById(R.id.random_course_category);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        randomCourseRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        courseAdapter = new CourseAdapter(Home.this, getContext(), new ViewModelProvider(Home.this).get(ReviewViewModel.class), getViewLifecycleOwner());

        // DATA-SCIENCE CATEGORY
        data_science_category_recyclerview = view.findViewById(R.id.data_science_category_recyclerview);


        // PROGRAMMING CATEGORY
        programming_category_recyclerview = view.findViewById(R.id.programming_category_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
        programming_category_recyclerview.setLayoutManager(gridLayoutManager);

        // OBSERVE THE LIVE DATA OF COURSE CATEGORIES
        courseViewModel.getDistinctCategory().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {

                adapter = new CourseCategoriesAdapter(Home.this);
                adapter.setCategories(strings);
                categoryRecyclerView.setAdapter(adapter);
                categoryRecyclerView.setHasFixedSize(true);

                if (!strings.isEmpty()) {
                    shimmerFrameLayoutCategory.stopShimmer();
                    shimmerFrameLayoutCategory.setVisibility(View.GONE);
                }


            }
        });


        //OBSERVE THE LIVE DATA OF COURSE FOR RANDOM DATA
        courseViewModel.getCourseByRandomLimit4().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {

                if (!courses.isEmpty()) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }

                randomCourseAdapter = new RandomCourseAdapter(Home.this, getContext(), new ViewModelProvider(Home.this).get(ReviewViewModel.class), getViewLifecycleOwner());
                randomCourseAdapter.setCategories(courses);
                randomCourseRecyclerView.setAdapter(randomCourseAdapter);
                randomCourseRecyclerView.setHasFixedSize(true);

            }
        });


        //OBSERVE THE LIVE DATA OF DATA-SCIENCE CATEGORY
        courseViewModel.getCourseByCategory("Data Science", 4).observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {


                courseAdapter.setCategories(courses);
                data_science_category_recyclerview.setAdapter(courseAdapter);
                data_science_category_recyclerview.setHasFixedSize(true);
                binding.sa.setVisibility(View.VISIBLE);
            }
        });


        //OBSERVE THE LIVE DATA OF PROGRAMMING CATEGORY
        courseViewModel.getCourseByCategory("Programming", 4).observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {

                courseAdapter.setCategories(courses);
                programming_category_recyclerview.setAdapter(courseAdapter);
                programming_category_recyclerview.setHasFixedSize(true);
                binding.prog.setVisibility(View.VISIBLE);
            }
        });


        gotNotification.setOnClickListener(n -> {
            startActivity(new Intent(getContext(), Notification.class));
        });


        new UserService().execute(new Pair<>(getContext(), token, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String userId = jsonObject.getString("userId");
                    String socialId = jsonObject.getString("socialId");
                    String firstName = jsonObject.getString("firstName");
                    String lastName = jsonObject.getString("lastName");
                    String email = jsonObject.getString("email");
                    String role = jsonObject.getString("role");
                    boolean emailValid = jsonObject.getBoolean("emailValid");
                    editor.putString("id", userId).putString("firstName", firstName).putString("lastName", lastName).putString("email", email).commit();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                editor.clear().commit();
            }
        }));


        // IF NEW DATA EXISTS IN DATABASE THEN UPDATE ROOM DATABASE WITH NEW DATA
        courseViewModel.refreshDatabase();
        return view;
    }

    @Override
    public void onItemClick(String courseCategory) {
        startActivity(new Intent(getContext(), CourseCategoryList.class).putExtra("courseCategory", courseCategory));
    }

    @Override
    public void onItemClick(Course course) {
        startActivity(new Intent(getContext(), CourseDetail.class).putExtra("courseId", course.getCourseId()));
    }
}