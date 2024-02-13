package com.example.coursehub.room.repository;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.coursehub.environemnt.Environment;
import com.example.coursehub.room.dao.WishListDao;
import com.example.coursehub.room.database.CourseDatabase;
import com.example.coursehub.room.entities.WishList;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class WishListRepository {

    private WishListDao wishListDao;
    private ExecutorService executorService;

    public WishListRepository(Application application) {
        CourseDatabase database = CourseDatabase.getInstance(application);
        wishListDao = database.wishListDao();
        executorService = Executors.newSingleThreadExecutor(); // Create a single-thread executor
    }


    private void isCourseInWishlist(String courseId, String userId, Consumer<Boolean> callback) {
        executorService.submit(() -> {
            try {
                List<Boolean> result = wishListDao.isCourseInWishlist(courseId, userId);
                boolean isCourseInWishlist = result != null && !result.isEmpty() && result.get(0);
                callback.accept(isCourseInWishlist);
            } catch (Exception e) {
                // Handle exceptions
                e.printStackTrace();
                callback.accept(false); // Signal failure to callback
            }
        });


    }


    public LiveData<Boolean> isCourseInWishlistCheck(WishList wishList){
        String c = String.valueOf(wishList.getCourseId());
        String u = String.valueOf(wishList.getUserId());

        return wishListDao.isCourseInWishlistLiveData(c, u);
    }


    public void insert(WishList wishList, Context context, LifecycleOwner lifecycleOwner) {

        String c = String.valueOf(wishList.getCourseId());
        String u = String.valueOf(wishList.getUserId());

        isCourseInWishlist(c, u, isCourseInWishlist -> {
            if (isCourseInWishlist) {
                //Toast messages must be displayed from the main (UI) thread.
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Course already exists in wishlist", Toast.LENGTH_SHORT).show();
                });
            } else {
                new InsertWishListAsyncTask(wishListDao).execute(wishList);
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Course added successfully", Toast.LENGTH_SHORT).show();
                });
                InsertCourseToWishList(context, wishList.getUserId(), wishList.getCourseId());
            }
        });

    }


    private static class InsertWishListAsyncTask extends AsyncTask<WishList, Void, Void> {

        private WishListDao wishListDao;

        public InsertWishListAsyncTask(WishListDao wishListDao) {
            this.wishListDao = wishListDao;
        }

        @Override
        protected Void doInBackground(WishList... wishLists) {
            wishListDao.insert(wishLists[0]);
            return null;
        }
    }

    public void InsertCourseToWishList(Context context, Long user, Long course) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final String url = Environment.getBaseUrl() + "wishlist";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", user);
            jsonObject.put("CourseId", course);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i(TAG, "Course added api database to User Wishlist user:{}  ");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String responseString = new String(networkResponse.data, StandardCharsets.UTF_8);
                    Log.e("ErrorResponse", "Response data: " + responseString);
                    return;
                }
                Log.e(TAG, "Volley Error: " + volleyError.getMessage());
                return;
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}
