package com.boiseboise.redditslash;

import android.graphics.drawable.Drawable;

/**
 * Created by nicke on 6/28/2017.
 */

public class LinkPost extends Post {

    private String mThumbnailURL;
    private Drawable mThumbnail;

    public LinkPost(String title, String thumbnailURL, Drawable thumbnail) {
        super(title);
        setPostType(Post.LINK_POST);
        mThumbnailURL = thumbnailURL;
        mThumbnail = thumbnail;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public void setmThumbnailURL(String thumbnailURL) {
        mThumbnailURL = thumbnailURL;
    }

    public Drawable getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Drawable thumbnail){
        mThumbnail = thumbnail;
    }
}
