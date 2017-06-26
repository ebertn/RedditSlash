package com.boiseboise.redditslash;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SubredditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Post>> {

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private PostAdapter mPostAdapter;
    private String SUBREDDIT_REQUEST_URL = "https://www.reddit.com/r/all/.json";
    private String LOG_TAG = this.getClass().getSimpleName();
    private TextView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddit);

        // Create listview using a post adapter
        mPostAdapter = new PostAdapter(this, new ArrayList<Post>());
        ListView postListView = (ListView) findViewById(R.id.list);
        postListView.setAdapter(mPostAdapter);

        mEmptyView = (TextView) findViewById(R.id.empty);
        postListView.setEmptyView(mEmptyView);

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.v(LOG_TAG, "------ Initing loader");
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
            Log.v(LOG_TAG, "------ LOADER INITED");
        } else {
            View loadingIndicator = findViewById(R.id.loading_circle);
            loadingIndicator.setVisibility(View.GONE);

            // Set empty state text to display "No earthquakes found."
            mEmptyView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Post>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "onCreateLoader");
        // Create a new loader for the given URL
        return new PostLoader(this, SUBREDDIT_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Post>> loader, List<Post> posts) {
        Log.v(LOG_TAG, "onLoadFinished");
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_circle);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyView.setText(R.string.empty_text);
        // Clear the adapter of previous earthquake data
        mPostAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (posts != null && !posts.isEmpty()) {
            mPostAdapter.addAll(posts);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Post>> loader) {
        Log.v(LOG_TAG, "onLoaderReset");
        // Loader reset, so we can clear out our existing data.
        mPostAdapter.clear();
    }
}
