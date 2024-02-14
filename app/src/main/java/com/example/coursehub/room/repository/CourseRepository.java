package com.example.coursehub.room.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.coursehub.environemnt.Environment;
import com.example.coursehub.room.dao.CourseDao;
import com.example.coursehub.room.database.CourseDatabase;
import com.example.coursehub.room.entities.Course;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CourseRepository {

    private CourseDao courseDao;
    private LiveData<List<Course>> allCourses;

    private LiveData<List<Course>> randomLimit4;

    private RequestQueue requestQueue;

    public CourseRepository(Application application) {
        CourseDatabase database = CourseDatabase.getInstance(application);
        this.requestQueue = Volley.newRequestQueue(application.getApplicationContext());
        courseDao = database.courseDao();
        randomLimit4 = courseDao.getCourseByRandomLimit4();
        allCourses = courseDao.getAllCourses();
    }

    public void update(Course course) {
        new UpdateCourseAsyncTask(courseDao).execute(course);
    }

    public void insertAll(List<Course> course) {
        new InsertAllCourseAsyncTask(courseDao).execute(course);
    }

    public void delete(Course course) {
        new DeleteCourseAsyncTask(courseDao).execute(course);
    }

    public void deleteAllCourse() {
        new DeleteAllCourseAsyncTask(courseDao).execute();
    }

    public LiveData<List<Course>> getCourseByRandomLimit4() {
        return randomLimit4;
    }

    public LiveData<List<Course>> getAllCourse() {
        return allCourses;
    }

    public LiveData<List<String>> getDistinctCategory() {
        return courseDao.getDistinctCategory();
    }

    public LiveData<List<Course>> getCourseByCategory(String category, int limit) {
        return courseDao.getCourseByCategory(category, limit);
    }

    public LiveData<Course> getCourseById(Long category) {
        return courseDao.getCourseById(category);
    }

    public LiveData<List<Course>> searchCourses(String query) {
        return courseDao.searchCourses(query);
    }


    private static class InsertAllCourseAsyncTask extends AsyncTask<List<Course>, Void, Void> {

        private CourseDao courseDao;

        public InsertAllCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(List<Course>... lists) {
            courseDao.insertCourses(lists[0]);
            return null;
        }
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<Course, Void, Void> {

        private CourseDao courseDao;

        public DeleteCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.delete(courses[0]);
            return null;
        }
    }

    private static class DeleteAllCourseAsyncTask extends AsyncTask<Void, Void, Void> {

        private CourseDao courseDao;

        public DeleteAllCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            courseDao.deleteAllCourse();
            return null;
        }

    }

    private static class UpdateCourseAsyncTask extends AsyncTask<Course, Void, Void> {

        private CourseDao courseDao;

        public UpdateCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.update(courses[0]);
            return null;
        }

    }

    private static class getNumberOfItemsAsyncTask extends AsyncTask<Course, Void, Integer> {

        private CourseDao courseDao;
        int a;

        public getNumberOfItemsAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Integer doInBackground(Course... courses) {
            int a = courseDao.getNumberOfItems();
            return a;
        }
    }


    public void refreshDatabase() {
        List<Course> c = new ArrayList<>();

        final String url = Environment.getBaseUrl() + "courses";

        // Fetch data from the API using JsonArrayRequest
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject item = response.getJSONObject(i);
                        String courseId = item.getString("courseId");
                        String name = item.getString("name");
                        String instructor = item.getString("instructor");
                        String duration = item.getString("duration");
                        String date = item.getString("date");
                        String time = item.getString("time");
                        String location = item.getString("location");
                        String price = item.getString("price");
                        String description = item.getString("description");
                        String category = item.getString("category");
                        String imageUrl = item.getString("imageUrl");

                        c.add(new Course(Long.parseLong(courseId), name, instructor, duration, date, time, location, Double.parseDouble(price), description, category, imageUrl));


                    }
                    if (shouldRefreshData(c)) {
                        insertAll(c);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Execute database insertion asynchronously
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        error.printStackTrace();
                        System.out.println("error");
                    }
                }
        );

        // Add the request to the request queue
        requestQueue.add(jsonArrayRequest);

    }


    private boolean shouldRefreshData(List<Course> apiCourses) throws ExecutionException, InterruptedException {

        int apiItemCount = apiCourses.size();
        int roomItemCount = new getNumberOfItemsAsyncTask(courseDao).execute().get();

        return apiItemCount > roomItemCount;
    }


}
