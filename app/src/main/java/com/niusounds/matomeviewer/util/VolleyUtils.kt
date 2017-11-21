package com.niusounds.matomeviewer.util

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.util.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.androidannotations.annotations.AfterInject
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext

@EBean(scope = EBean.Scope.Singleton)
open class VolleyUtils {
    private val header = mapOf(Pair("User-Agent", "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19"))

    private lateinit var queue: RequestQueue
    lateinit var defaultImageLoader: ImageLoader

    @RootContext
    lateinit var ctx: Context

    @AfterInject
    fun init() {
        queue = Volley.newRequestQueue(ctx)
        defaultImageLoader = ImageLoader(queue, object : ImageLoader.ImageCache {
            private val cache = LruCache<String, Bitmap>(20)

            override fun getBitmap(url: String): Bitmap? = cache.get(url)

            override fun putBitmap(url: String, bitmap: Bitmap) {
                cache.put(url, bitmap)
            }
        })
    }

    fun requestString(url: String, listener: Response.Listener<String>, errorListener: Response.ErrorListener): Request<String> {
        val request = object : StringRequest(url, listener, errorListener) {
            override fun getHeaders(): Map<String, String> = header
        }
        queue.add(request)
        return request
    }
}
