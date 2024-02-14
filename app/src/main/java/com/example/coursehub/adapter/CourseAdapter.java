package com.example.coursehub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.coursehub.R;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.entities.Reviews;
import com.example.coursehub.room.viewmodel.ReviewViewModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private static int grid = 2;
    private static int linear = 1;
    private boolean isLinearLayout;
    List<Course> list;

    ReviewViewModel reviewViewModel;

    LifecycleOwner lifecycleOwner;

    ItemClickListener itemClickListener;

    private final Context context;
    private int selectedItem = 0; // Store the selected item position

    public void setCategories(List<Course> list) {
        this.list = list;
    }

    public CourseAdapter(ItemClickListener itemClickListener, Context context, ReviewViewModel reviewViewModel, LifecycleOwner lifecycleOwner) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.reviewViewModel = reviewViewModel;
        this.lifecycleOwner = lifecycleOwner;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view;
        if (viewType == grid){
            view = inflater.inflate(R.layout.course_list_design, parent, false);
        }else {
           view= inflater.inflate(R.layout.course_list_design, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).getImageUrl())
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(16)))
                .into(holder.courseImg);
        holder.name_of_course.setText(list.get(position).getName());
        holder.instructor.setText(list.get(position).getInstructor());
        Double d = list.get(position).getPrice();
        String COUNTRY = "US";
        String LANGUAGE = "en";
        String str = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(d);

        reviewViewModel.getReviewsById(list.get(position).getCourseId()).observe(lifecycleOwner, new Observer<List<Reviews>>() {
            @Override
            public void onChanged(List<Reviews> reviews) {

                if (reviews == null || reviews.isEmpty()) {
                    holder.rating.setText(0);
                    return;
                }

                int totalRatings = 0;
                int sumOfRatings = 0;

                for (Reviews rating : reviews) {
                    sumOfRatings += rating.getRating();
                    totalRatings++;
                }

                double t = (double) sumOfRatings / totalRatings;
                int filledStars = (int) t;
                String a = String.valueOf(t);
                holder.rating.setText(a);


                for (int i = 0; i < filledStars; i++) {
                    addStar(holder.stars_layout, R.drawable.star); // Add filled stars
                }
            }
        });



        holder.price.setText(str);

        holder.courseImg.setOnClickListener(v -> {
            itemClickListener.onItemClick(list.get(position));
        });

    }


    @Override
    public int getItemCount() {
        if (list.isEmpty() || list == null){
            return 0;
        }else{
            return list.size();
        }

    }

    @Override
    public int getItemViewType(int position) {
        return isLinearLayout ? linear : grid;
    }

    private void addStar(LinearLayout layout, int drawableId) {
        ImageView star = new ImageView(context);
        star.setImageResource(drawableId);
        layout.addView(star);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView courseImg;
        TextView name_of_course, instructor, rating, price;
        LinearLayout stars_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseImg = itemView.findViewById(R.id.courseImg);
            name_of_course = itemView.findViewById(R.id.name_of_course);
            instructor = itemView.findViewById(R.id.instructor);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
            stars_layout =  itemView.findViewById(R.id.stars_layout);
        }
    }

    public interface ItemClickListener {
        void onItemClick(Course course);
    }
}


