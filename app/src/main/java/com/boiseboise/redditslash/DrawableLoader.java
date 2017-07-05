package com.boiseboise.redditslash;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by nicke on 6/30/2017.
 */

public class DrawableLoader extends AsyncTaskLoader<Drawable> {

    private String mUrl;
    public static final String LOG_TAG = DrawableLoader.class.getName();


    public DrawableLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    public Drawable loadInBackground() {
        Log.v(LOG_TAG, "loadInBackground");
        if(mUrl == null){
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        return QueryUtils.loadImageFromWeb(mUrl);
    }
}
