package com.boiseboise.redditslash;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return an {@link Post} object to represent a single earthquake.
     */
    public static ArrayList<Post> fetchPostData(String requestUrl) {

        Log.v(LOG_TAG, "fetchEarthquakeData");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
         return extractFeatureArrayFromJson(jsonResponse);
    }

    public static Drawable loadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Post} object by parsing out information
     * about the first post from the input earthquakeJSON string.
     */
    private static ArrayList<Post> extractFeatureArrayFromJson(String postJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(postJSON)) {
            return null;
        }

        try {
            JSONObject baseJsonResponse = new JSONObject(postJSON);
            JSONArray postDataArray = baseJsonResponse
                    .getJSONObject("data")
                    .getJSONArray("children");

            // If there are results in the features array
            if (postDataArray.length() > 0) {

                String title, thumbnailURL;
                JSONObject currentPost;

                // Create an empty ArrayList that we can start adding earthquakes to
                ArrayList<Post> posts = new ArrayList<>();
                Drawable d;

                for(int i = 0; i < postDataArray.length(); i++) {
                    // Extract out the first feature (which is an earthquake)
                    currentPost = postDataArray.getJSONObject(i).getJSONObject("data");
                    title = currentPost.getString("title");
                    // TODO: get rid of this try/catch because its super jank and will fail if there is a real JSONException
                    try {
                        thumbnailURL = currentPost
                                .getString("thumbnail");

                        posts.add(new LinkPost(title, thumbnailURL, null));
                    } catch (JSONException e1) {
                        posts.add(new Post(title));

                    }
                }

                // Create a new {@link post} object
                return posts;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the post JSON results", e);
        }
        return null;
    }

}
