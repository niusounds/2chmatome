package com.niusounds.matomeviewer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.android.volley.toolbox.NetworkImageView
import com.niusounds.matomeviewer.data.Article
import com.niusounds.matomeviewer.event.ArticleClickEvent
import com.niusounds.matomeviewer.util.VolleyUtils
import de.greenrobot.event.EventBus
import org.androidannotations.annotations.AfterInject
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import org.androidannotations.annotations.res.DimensionPixelSizeRes

@EViewGroup(R.layout.item)
open class ItemView(context: Context) : FrameLayout(context), View.OnClickListener {
    private var article: Article? = null

    @ViewById
    lateinit var image: NetworkImageView
    @ViewById
    lateinit var title: TextView

    @Bean
    lateinit var v: VolleyUtils

    @DimensionPixelSizeRes
    @JvmField
    var articleListPadding: Int = 0

    init {
        layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        setOnClickListener(this)
    }

    @AfterInject
    fun init() {
        setPadding(0, articleListPadding, 0, articleListPadding)
    }

    fun bind(article: Article) {
        this.article = article
        title.text = article.title
        image.setImageUrl(article.imageUrl, v.defaultImageLoader)
    }

    override fun onClick(view: View) {
        if (article != null) {
            EventBus.getDefault().post(ArticleClickEvent(article!!))
        }
    }
}
