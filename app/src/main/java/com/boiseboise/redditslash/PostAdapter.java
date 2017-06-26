package com.boiseboise.redditslash;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicke on 6/25/2017.
 */

public class PostAdapter extends ArrayAdapter<Post> {

    public PostAdapter(@NonNull Context context, @NonNull ArrayList<Post> postList) {
        super(context, 0, postList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.post_list_item, parent, false);
        }

        Post currentEarthquake = this.getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.post_title);
        title.setText(currentEarthquake.getTitle());

        return listItemView;
    }
}
