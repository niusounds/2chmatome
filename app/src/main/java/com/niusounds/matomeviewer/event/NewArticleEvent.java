package com.niusounds.matomeviewer.event;

import com.niusounds.matomeviewer.data.Article;

public class NewArticleEvent {
    public final Article article;

    public NewArticleEvent(Article article) {
        this.article = article;
    }
}
