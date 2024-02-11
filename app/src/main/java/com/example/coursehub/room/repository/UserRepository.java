package com.example.coursehub.room.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.android.volley.toolbox.Volley;
import com.example.coursehub.room.dao.CourseDao;
import com.example.coursehub.room.dao.UserDao;
import com.example.coursehub.room.database.CourseDatabase;
import com.example.coursehub.room.entities.Course;
import com.example.coursehub.room.entities.User;

public class UserRepository {

    private UserDao userDao;

    public UserRepository(Application application) {
        CourseDatabase database = CourseDatabase.getInstance(application);
        userDao = database.userDao();
    }

    public void update(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    public void insert(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }


    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        public UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }


        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        public InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }


        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }
}
