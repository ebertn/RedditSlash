package com.boiseboise.redditslash;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Nick on 6/22/2017.
 */

public class PostLoader extends AsyncTaskLoader<List<Post>> {

    private String mUrl;
    public static final String LOG_TAG = PostLoader.class.getName();

    public PostLoader(Context context, String url) {
        super(context);
        mUrl = url;
        Log.v(LOG_TAG, "EarthquakeLoader contructor");

    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    public List<Post> loadInBackground() {
        Log.v(LOG_TAG, "loadInBackground");
        if(mUrl == null){
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        return QueryUtils.fetchPostData(mUrl);
    }
}

