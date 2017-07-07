package com.boiseboise.redditslash;

/**
 * Created by Nick on 6/23/2017.
 */

public class Post {
    private String mTitle;
    private String mThumbnailURL = null;
    public static final int SELF_POST = 0;
    public static final int LINK_POST = 1;
    // Post type, where 0 = self post and 1 = link
    private int postType = SELF_POST;

    public Post(String title){
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public int getPostType() {
        return postType;
    }
}
