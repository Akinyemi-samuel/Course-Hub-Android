package com.example.coursehub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursehub.R;

import java.util.List;

public class CourseCategoriesAdapter extends RecyclerView.Adapter<CourseCategoriesAdapter.ViewHolder> {

    List<String> list;

    ItemClickListener itemClickListener;
    private int selectedItem = 0; // Store the selected item position

    public void setCategories(List<String> list) {
        this.list = list;
    }

    public CourseCategoriesAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_category_recyclerview_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.categoryNames.setText(list.get(position));

        holder.categoryNames.setOnClickListener(v -> {
            itemClickListener.onItemClick(list.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }else{
            return 0;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryNames;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryNames = itemView.findViewById(R.id.category);
        }
    }

    public interface ItemClickListener {
        void onItemClick(String courseCategory);
    }
}


