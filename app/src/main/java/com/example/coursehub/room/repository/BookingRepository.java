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
import com.example.coursehub.room.dao.BookingDao;
import com.example.coursehub.room.dao.WishListDao;
import com.example.coursehub.room.database.CourseDatabase;
import com.example.coursehub.room.entities.Booking;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.entities.WishList;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class BookingRepository {

    private BookingDao bookingDao;
    private ExecutorService executorService;

    public BookingRepository(Application application) {
        CourseDatabase database = CourseDatabase.getInstance(application);
        bookingDao = database.bookingDao();
        executorService = Executors.newSingleThreadExecutor(); // Creates a single-thread executor
    }

    public void deleteWishlistItem(Long userId, Long courseId) {
        executorService.execute(() -> bookingDao.deleteWishlistItem(userId, courseId));
    }

    public LiveData<List<Course>> getCoursesInWishlist(Long userId) {
        return bookingDao.getCoursesInWishlist(userId);
    }


    private void isCourseInWishlist(String courseId, String userId, Consumer<Boolean> callback) {
        executorService.submit(() -> {
            try {
                List<Boolean> result = bookingDao.isCourseInWishlist(courseId, userId);
                boolean isCourseInWishlist = result != null && !result.isEmpty() && result.get(0);
                callback.accept(isCourseInWishlist);
            } catch (Exception e) {
                // Handle exceptions
                e.printStackTrace();
                callback.accept(false); // Signal failure to callback
            }
        });


    }


    public LiveData<List<Booking>> getWishListByUserId(Long wishList) {
        return bookingDao.getWishListByUserId(wishList);
    }


    public LiveData<Boolean> isCourseInWishlistCheck(Booking booking) {
        String c = String.valueOf(booking.getCourse());
        String u = String.valueOf(booking.getUser());

        return bookingDao.isCourseInWishlistLiveData(c, u);
    }


    public void insert(Booking booking, Context context, LifecycleOwner lifecycleOwner) {

        String c = String.valueOf(booking.getCourse());
        String u = String.valueOf(booking.getUser());

        isCourseInWishlist(c, u, isCourseInWishlist -> {
            if (isCourseInWishlist) {
                //Toast messages must be displayed from the main (UI) thread.
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Course has already been booked", Toast.LENGTH_SHORT).show();
                });
            } else {
                new InsertWishListAsyncTask(bookingDao).execute(booking);
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Course booked successfully", Toast.LENGTH_SHORT).show();
                });
                InsertCourseToWishListApiDatabase(context, booking.getUser(), booking.getCourse());
            }
        });

    }


    private static class InsertWishListAsyncTask extends AsyncTask<Booking, Void, Void> {

        private BookingDao wishListDao;

        public InsertWishListAsyncTask(BookingDao wishListDao) {
            this.wishListDao = wishListDao;
        }

        @Override
        protected Void doInBackground(Booking... wishLists) {
            wishListDao.insert(wishLists[0]);
            return null;
        }
    }

    public void InsertCourseToWishListApiDatabase(Context context, Long user, Long course) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final String url = Environment.getBaseUrl() + "booking";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", user);
            jsonObject.put("course", course);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i(TAG, "Course added api database to User booking user:{}  ");
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
