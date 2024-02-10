package com.example.coursehub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.coursehub.R;
import com.example.coursehub.room.entities.Course;

import java.util.List;

public class RandomCourseAdapter extends RecyclerView.Adapter<RandomCourseAdapter.ViewHolder> {

    List<Course> list;

    ItemClickListener itemClickListener;
    private final Context context;
    private int selectedItem = 0; // Store the selected item position

    public void setCategories(List<Course> list) {
        this.list = list;
    }

    public RandomCourseAdapter(ItemClickListener itemClickListener, Context context) {
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.random_course_home_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).getImageUrl())
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(16)))
                .into(holder.courseImg);
        holder.name_of_course.setText(list.get(position).getName());
        holder.description.setText(list.get(position).getDescription());
        holder.instructor.setText(list.get(position).getInstructor());

        holder.courseImg.setOnClickListener(v -> {
            itemClickListener.onItemClick(list.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView courseImg;
        TextView name_of_course, description, instructor, rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            courseImg = itemView.findViewById(R.id.courseImg);
            name_of_course = itemView.findViewById(R.id.name_of_course);
            description = itemView.findViewById(R.id.decription);
            instructor = itemView.findViewById(R.id.instructor);
            rating = itemView.findViewById(R.id.rating);
        }
    }

    public interface ItemClickListener {
        void onItemClick(Course course);
    }
}


