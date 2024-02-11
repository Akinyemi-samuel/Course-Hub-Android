package com.example.coursehub.room.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.coursehub.room.dao.WishListDao;
import com.example.coursehub.room.database.CourseDatabase;
import com.example.coursehub.room.entities.WishList;

public class WishListRepository {

    private WishListDao wishListDao;

    public WishListRepository(Application application) {
        CourseDatabase database = CourseDatabase.getInstance(application);
        wishListDao = database.wishListDao();
    }


    public void insert(WishList wishList) {
        new InsertWishListAsyncTask(wishListDao).execute(wishList);
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
}
