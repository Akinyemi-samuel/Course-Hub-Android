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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.coursehub.R;
import com.example.coursehub.room.entities.Reviews;
import com.example.coursehub.room.viewmodel.ReviewViewModel;
import com.example.coursehub.service.AuthService;
import com.example.coursehub.service.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    List<Reviews> list;

    ReviewViewModel reviewViewModel;

    LifecycleOwner lifecycleOwner;
    private final Context context;
    private int selectedItem = 0; // Store the selected item position

    public void setCategories(List<Reviews> list) {
        this.list = list;
    }

    public ReviewAdapter(Context context, ReviewViewModel reviewViewModel, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.reviewViewModel = reviewViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_recycler_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.comment.setText(list.get(position).getComment());


        new AuthService(context).execute(new Pair<>(list.get(position).getUser(), context, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String firstName = jsonObject.getString("firstName");
                    String lastName = jsonObject.getString("lastName");

                    holder.name_of_user.setText(firstName + " " + lastName);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }));


        reviewViewModel.getReviewsById(list.get(position).getCourse()).observe(lifecycleOwner, new Observer<List<Reviews>>() {
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

        TextView name_of_user, comment, rating;

        LinearLayout stars_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name_of_user = itemView.findViewById(R.id.name_of_user);
            rating = itemView.findViewById(R.id.rating);
            comment = itemView.findViewById(R.id.comment);
            stars_layout = itemView.findViewById(R.id.stars_layout);
        }
    }

}


