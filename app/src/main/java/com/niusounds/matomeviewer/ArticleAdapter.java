package com.niusounds.matomeviewer;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.niusounds.matomeviewer.data.Article;

import org.androidannotations.annotations.EBean;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

@EBean
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Article> articles = Collections.emptyList();

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(ItemView_.build(viewGroup.getContext()));
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int i) {
        viewHolder.view.bind(articles.get(i));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemView view;

        public ViewHolder(ItemView itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}
