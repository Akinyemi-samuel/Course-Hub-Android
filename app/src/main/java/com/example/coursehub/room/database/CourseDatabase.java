package com.example.coursehub.room.database;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import com.example.coursehub.dto.Review;
import com.example.coursehub.environemnt.Environment;
import com.example.coursehub.room.dao.CourseDao;
import com.example.coursehub.room.dao.UserDao;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.entities.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Course.class, User.class}, version = 3)
public abstract class CourseDatabase extends RoomDatabase {

    private static CourseDatabase instance;

    public abstract CourseDao courseDao();

    public abstract UserDao userDao();

    public static synchronized CourseDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), CourseDatabase.class,
                    "course").fallbackToDestructiveMigration().addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

        //    new PopulateDatabaseAsyncTask(instance).execute();
        }
    };

    private static class PopulateDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {

        private CourseDao courseDao;

        public PopulateDatabaseAsyncTask(CourseDatabase db) {
            this.courseDao = db.courseDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

    }

}
