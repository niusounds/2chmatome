package com.niusounds.matomeviewer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean(scope = EBean.Scope.Singleton)
public class VolleyUtils {
    private RequestQueue queue;
    private ImageLoader imageLoader;

    @RootContext
    Context ctx;

    @AfterInject
    void init() {
        queue = Volley.newRequestQueue(ctx);
        imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public <T> Request<T> request(String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        GsonRequest<T> request = new GsonRequest<>(url, clazz, null, listener, errorListener);
        queue.add(request);
        return request;
    }

    public Request<Bitmap> image(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
        ImageRequest request = new ImageRequest(url, listener, maxWidth, maxHeight, decodeConfig, errorListener);
        queue.add(request);
        return request;
    }

    public Request<String> requestString(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(url, listener, errorListener);
        queue.add(request);
        return request;
    }

    public ImageLoader getDefaultImageLoader() {
        return imageLoader;
    }
}
