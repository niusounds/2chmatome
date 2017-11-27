package com.niusounds.matomeviewer

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.niusounds.matomeviewer.data.Article

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    var articles: List<Article> = emptyList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ArticleAdapter.ViewHolder {
        return ViewHolder(ItemView_.build(viewGroup.context))
    }

    override fun onBindViewHolder(viewHolder: ArticleAdapter.ViewHolder, i: Int) {
        viewHolder.view.bind(articles[i])
    }

    override fun getItemCount(): Int = articles.size

    class ViewHolder(var view: ItemView) : RecyclerView.ViewHolder(view)
}
