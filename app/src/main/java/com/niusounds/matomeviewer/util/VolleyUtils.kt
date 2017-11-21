package com.niusounds.matomeviewer.util

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.util.LruCache
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.androidannotations.annotations.AfterInject
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.util.*

@EBean(scope = EBean.Scope.Singleton)
open class VolleyUtils {
    private var queue: RequestQueue? = null
    var defaultImageLoader: ImageLoader? = null
        private set
    private val header = HashMap<String, String>()

    @RootContext
    lateinit var ctx: Context

    @AfterInject
    fun init() {
        queue = Volley.newRequestQueue(ctx)
        defaultImageLoader = ImageLoader(queue, object : ImageLoader.ImageCache {
            private val cache = LruCache<String, Bitmap>(20)

            override fun getBitmap(url: String): Bitmap? {
                return cache.get(url)
            }

            override fun putBitmap(url: String, bitmap: Bitmap) {
                cache.put(url, bitmap)
            }
        })

        header.put("User-Agent", "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19")
    }

    fun image(url: String, listener: Response.Listener<Bitmap>, maxWidth: Int, maxHeight: Int, decodeConfig: Bitmap.Config, errorListener: Response.ErrorListener): Request<Bitmap> {
        val request = ImageRequest(url, listener, maxWidth, maxHeight, decodeConfig, errorListener)
        queue!!.add(request)
        return request
    }

    fun requestString(url: String, listener: Response.Listener<String>, errorListener: Response.ErrorListener): Request<String> {
        val request = object : StringRequest(url, listener, errorListener) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                return header
            }
        }
        queue!!.add(request)
        return request
    }
}
