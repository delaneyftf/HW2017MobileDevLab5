package com.diglesia.hw2017mobiledev.lab5;

import org.json.JSONException;
import org.json.JSONObject;

public class Article {
    protected String mTitle;
    protected String mBodyExtract;
    protected String mImageURLString;
    protected String mURLString;

    public Article(JSONObject articleObj) {
        try {
            // We expect that these two keys will be in the response.
            mTitle = articleObj.getString("title");
            mURLString = articleObj.getString("fullurl");

            // There may or may not be a body text snippet in the response, so check before trying
            // to use it.
            if (articleObj.has("extract")) {
                mBodyExtract = articleObj.getString("extract");
            }

            // There may or may not be a thumbnail entry in the response, so check before trying
            // to use it.
            if (articleObj.has("thumbnail")) {
                mImageURLString = articleObj.getJSONObject("thumbnail").getString("source");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBodyExtract() {
        return mBodyExtract;
    }

    public String getImageURLString() {
        return mImageURLString;
    }

    public String getURLString() {
        return mURLString;
    }
}
