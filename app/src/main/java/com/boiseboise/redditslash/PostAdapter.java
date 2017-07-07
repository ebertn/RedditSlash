package com.boiseboise.redditslash;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicke on 6/25/2017.
 */

public class PostAdapter extends ArrayAdapter<Post> {

    public static final String LOG_TAG = PostAdapter.class.getName();

    public PostAdapter(@NonNull Context context, @NonNull ArrayList<Post> postList) {
        super(context, 0, postList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.link_list_item, parent, false);
        }

        TextView title = (TextView) listItemView.findViewById(R.id.post_title);

        Post currentPost = this.getItem(position);
        title = (TextView) listItemView.findViewById(R.id.post_title);

        title.setText(currentPost.getTitle());

        ImageView thumbnail = (ImageView) listItemView.findViewById(R.id.thumbnail);

        if (this.getItem(position).getPostType() == Post.LINK_POST) {
            thumbnail.setImageDrawable(((LinkPost) currentPost).getThumbnail());
        } else {
            thumbnail.setImageResource(R.mipmap.ic_launcher);
        }
        return listItemView;
    }


}
