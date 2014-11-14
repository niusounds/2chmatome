package com.niusounds.matomeviewer.event;

import com.niusounds.matomeviewer.data.Article;

public class ArticleClickEvent {
    public final Article article;

    public ArticleClickEvent(Article article) {
        this.article = article;
    }
}
