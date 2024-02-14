package com.example.coursehub.room.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.coursehub.environemnt.Environment;
import com.example.coursehub.room.dao.BookingDao;
import com.example.coursehub.room.dao.CourseDao;
import com.example.coursehub.room.dao.ReviewDao;
import com.example.coursehub.room.dao.UserDao;
import com.example.coursehub.room.dao.WishListDao;
import com.example.coursehub.room.entities.Booking;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.entities.Reviews;
import com.example.coursehub.room.entities.User;
import com.example.coursehub.room.entities.WishList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Database(entities = {Course.class, User.class, Booking.class, WishList.class, Reviews.class}, version = 2)
public abstract class CourseDatabase extends RoomDatabase {

    private static RequestQueue requestQueue;
    private static CourseDatabase instance;

    public abstract CourseDao courseDao();

    public abstract UserDao userDao();

    public abstract BookingDao bookingDao();

    public abstract WishListDao wishListDao();

    public abstract ReviewDao reviewDao();

    public static synchronized CourseDatabase getInstance(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), CourseDatabase.class,
                            "courseHub").fallbackToDestructiveMigration().addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDatabaseReviewAsyncTask(instance).execute();
        }
    };

    private static class PopulateDatabaseReviewAsyncTask extends AsyncTask<Void, Void, Void> {

        private CourseDao courseDao;

        public PopulateDatabaseReviewAsyncTask(CourseDatabase db) {
            this.courseDao = db.courseDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            refreshDatabase();
            return null;
        }

    }

    public static void insertAll(List<Reviews> reviews) {
        new InsertAllReviewAsyncTask(instance.reviewDao()).execute(reviews);
    }


    public static void refreshDatabase() {
        List<Reviews> c = new ArrayList<>();

        final String url = Environment.getBaseUrl() + "reviews";

        // Fetch data from the API using JsonArrayRequest
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject item = response.getJSONObject(i);
                        Long courseId = Long.valueOf(item.getString("course"));
                        Long userId = Long.valueOf(item.getString("user"));
                        String comment = item.getString("comment");
                        int rating = Integer.parseInt(item.getString("rating"));

                        c.add(new Reviews(courseId, userId, comment, rating));
                    }
                    if (shouldRefreshData(c)) {
                        insertAll(c);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        error.printStackTrace();
                        System.out.println("erroring");
                    }
                }
        );

        // Add the request to the request queue
        requestQueue.add(jsonArrayRequest);

    }


    private static class InsertAllReviewAsyncTask extends AsyncTask<List<Reviews>, Void, Void> {

        private ReviewDao reviewDao;

        public InsertAllReviewAsyncTask(ReviewDao reviewDao) {
            this.reviewDao = reviewDao;
        }

        @Override
        protected Void doInBackground(List<Reviews>... lists) {
            reviewDao.insertReviews(lists[0]);
            return null;
        }
    }

    private static class getNumberOfItemsAsyncTask extends AsyncTask<Reviews, Void, Integer> {

        private ReviewDao reviewDao;

        public getNumberOfItemsAsyncTask(ReviewDao reviewDao) {
            this.reviewDao = reviewDao;
        }

        @Override
        protected Integer doInBackground(Reviews... reviews) {
            int a = reviewDao.getNumberOfItems();
            return a;
        }
    }

    private static boolean shouldRefreshData(List<Reviews> reviews) throws ExecutionException, InterruptedException {

        int apiItemCount = reviews.size();
        int roomItemCount = new getNumberOfItemsAsyncTask(instance.reviewDao()).execute().get();

        return apiItemCount > roomItemCount;
    }


}
