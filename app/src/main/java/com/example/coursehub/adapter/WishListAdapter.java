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

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    List<Course> list;

    ItemClickListener itemClickListener;

    ReviewViewModel reviewViewModel;

    LifecycleOwner lifecycleOwner;
    private final Context context;
    private int selectedItem = 0; // Store the selected item position

    public void setCategories(List<Course> list) {
        this.list = list;
    }

    public WishListAdapter(ItemClickListener itemClickListener, Context context, ReviewViewModel reviewViewModel, LifecycleOwner lifecycleOwner) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.reviewViewModel = reviewViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).getImageUrl())
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(16)))
                .into(holder.courseImg);
        holder.name_of_course.setText(list.get(position).getName());
        holder.instructor.setText(list.get(position).getInstructor());
        //holder.price.setText(list.get(position).getName());
        Double d = list.get(position).getPrice();
        String COUNTRY = "US";
        String LANGUAGE = "en";
        String str = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(d);
        holder.price.setText(str);

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


        holder.courseImg.setOnClickListener(v -> {
            itemClickListener.onItemClick(list.get(position));
        });

        holder.removeCourse.setOnClickListener(v -> {
            itemClickListener.onItemClick2(list.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void addStar(LinearLayout layout, int drawableId) {
        ImageView star = new ImageView(context);
        star.setImageResource(drawableId);
        layout.addView(star);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView courseImg;
        TextView name_of_course, instructor, rating, price, removeCourse;

        LinearLayout stars_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseImg = itemView.findViewById(R.id.courseImg);
            name_of_course = itemView.findViewById(R.id.name_of_course);
            instructor = itemView.findViewById(R.id.instructor);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
            removeCourse = itemView.findViewById(R.id.remove_wishlist);
            stars_layout =  itemView.findViewById(R.id.stars_layout);
        }
    }

    public interface ItemClickListener {
        void onItemClick(Course course);

        void onItemClick2(Course course);


    }
}


