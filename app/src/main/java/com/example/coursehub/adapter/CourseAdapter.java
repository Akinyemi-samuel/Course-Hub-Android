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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    List<Course> list;

    ItemClickListener itemClickListener;

    private final Context context;
    private int selectedItem = 0; // Store the selected item position

    public void setCategories(List<Course> list) {
        this.list = list;
    }

    public CourseAdapter(ItemClickListener itemClickListener, Context context) {
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_design, parent, false);
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


        holder.price.setText(str);

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
        TextView name_of_course, instructor, rating, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseImg = itemView.findViewById(R.id.courseImg);
            name_of_course = itemView.findViewById(R.id.name_of_course);
            instructor = itemView.findViewById(R.id.instructor);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
        }
    }

    public interface ItemClickListener {
        void onItemClick(Course course);
    }
}


