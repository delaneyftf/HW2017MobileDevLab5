package com.diglesia.hw2017mobiledev.lab5;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WikipediaArticleSource {

    public interface ArticleListener {
        void onArticleResponse(List<Article> articleList);
    }

    private final static int IMAGE_CACHE_COUNT = 100;
    private final static int ARTICLE_REQUEST_COUNT = 25;
    private final static int ARTICLE_REQUEST_IMAGE_WIDTH = 400;
    private static WikipediaArticleSource sWikipediaArticleSourceInstance;

    private Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static WikipediaArticleSource get(Context context) {
        if (sWikipediaArticleSourceInstance == null) {
            sWikipediaArticleSourceInstance = new WikipediaArticleSource(context);
        }
        return sWikipediaArticleSourceInstance;
    }

    private WikipediaArticleSource(Context context) {
        mContext = context.getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(mContext);

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(IMAGE_CACHE_COUNT);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public void getArticles(ArticleListener articleListener) {
        final ArticleListener articleListenerInternal = articleListener;
        // URL for requesting articles from Wikipedia. This requests N random articles, with a short
        // text extract (rather than the whole article text), with a main thumbnail image of size M,
        // and with metadata including a link to the wikipedia article itself.
        String url = "https://en.wikipedia.org/w/api.php?format=json&action=query&generator=random&grnnamespace=0&prop=extracts|pageimages|info&exintro&explaintext&grnlimit="+ARTICLE_REQUEST_COUNT+"&piprop=thumbnail&pithumbsize="+ARTICLE_REQUEST_IMAGE_WIDTH+"&pilicense=any&inprop=url";
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Article> articleList = new ArrayList<Article>();
                            // Get the map of articles, keyed by article id.
                            JSONObject articlesObj = response.getJSONObject("query").getJSONObject("pages");
                            Iterator<String> it = articlesObj.keys();
                            while (it.hasNext()) {
                                String key = it.next();
                                JSONObject articleObject = articlesObj.getJSONObject(key);
                                Article article = new Article(articleObject);
                                articleList.add(article);
                            }
                            articleListenerInternal.onArticleResponse(articleList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            articleListenerInternal.onArticleResponse(null);
                            Toast.makeText(mContext, "Could not get articles.", Toast.LENGTH_SHORT);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        articleListenerInternal.onArticleResponse(null);
                        Toast.makeText(mContext, "Could not get articles.", Toast.LENGTH_SHORT);
                    }
                });

        mRequestQueue.add(jsonObjRequest);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
