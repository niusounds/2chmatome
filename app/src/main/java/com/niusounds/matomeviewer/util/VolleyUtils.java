package com.niusounds.matomeviewer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

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

    public <T> void request(String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        queue.add(new GsonRequest<T>(url, clazz, null, listener, errorListener));
    }

    public void image(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
        queue.add(new ImageRequest(url, listener, maxWidth, maxHeight, decodeConfig, errorListener));
    }

    public ImageLoader getDefaultImageLoader() {
        return imageLoader;
    }

    public void requestString(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        queue.add(new StringRequest(url, listener, errorListener));
    }
}
