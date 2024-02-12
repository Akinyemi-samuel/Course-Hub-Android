package com.example.coursehub.service;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;

public class MyAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Specify the disk cache directory and size
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        File diskCacheDir = context.getCacheDir();
        builder.setDiskCache(new DiskLruCacheFactory(diskCacheDir.getAbsolutePath(), diskCacheSizeBytes));
    }
}
