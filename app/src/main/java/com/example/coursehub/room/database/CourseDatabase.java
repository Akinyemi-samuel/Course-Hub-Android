package com.example.coursehub.room.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.coursehub.room.dao.BookingDao;
import com.example.coursehub.room.dao.CourseDao;
import com.example.coursehub.room.dao.UserDao;
import com.example.coursehub.room.dao.WishListDao;
import com.example.coursehub.room.entities.Booking;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.entities.User;
import com.example.coursehub.room.entities.WishList;

@Database(entities = {Course.class, User.class, Booking.class, WishList.class}, version = 1)
public abstract class CourseDatabase extends RoomDatabase {

    private static CourseDatabase instance;

    public abstract CourseDao courseDao();

    public abstract UserDao userDao();

    public abstract BookingDao bookingDao();

    public abstract WishListDao wishListDao();

    public static synchronized CourseDatabase getInstance(Context context) {
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
