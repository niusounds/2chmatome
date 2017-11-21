package com.niusounds.matomeviewer

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

import com.niusounds.matomeviewer.data.Article

import org.androidannotations.annotations.EBean

import java.util.Collections
import java.util.Date
import java.util.TreeMap

@EBean
open class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
    private var articles = emptyList<Article>()

    fun setArticles(articles: List<Article>) {
        this.articles = articles
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ArticleAdapter.ViewHolder {
        return ViewHolder(ItemView_.build(viewGroup.context))
    }

    override fun onBindViewHolder(viewHolder: ArticleAdapter.ViewHolder, i: Int) {
        viewHolder.view.bind(articles[i])
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    class ViewHolder(internal var view: ItemView) : RecyclerView.ViewHolder(view)
}
