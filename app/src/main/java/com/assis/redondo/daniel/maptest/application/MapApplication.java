package com.assis.redondo.daniel.maptest.application;

import android.app.Application;

import com.google.android.gms.maps.MapsInitializer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by DT on 10/8/15.
 */
public class MapApplication extends Application {

    int connectTimeout = 30000;
    int readTimeout = 30000;

    @Override
    public void onCreate() {
        super.onCreate();
        MapsInitializer.initialize(getApplicationContext());

        if(!ImageLoader.getInstance().isInited()) {
            DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                    .defaultDisplayImageOptions(opts)
                    .diskCacheFileCount(100)
                    .imageDownloader(new BaseImageDownloader(this,connectTimeout,readTimeout))
                    .build();
            ImageLoader.getInstance().init(config);

        }

    }
}
