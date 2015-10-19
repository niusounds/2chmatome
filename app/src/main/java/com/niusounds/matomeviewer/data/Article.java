package com.niusounds.matomeviewer.data;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable, Comparable<Article> {
    public String link;
    public String title;
    public String imageUrl;
    public String description;
    public Date date;
    public String pubDate;
    public String contentEncoded;

    @Override
    public int compareTo(Article article) {
        if (article == null || article.date == null || date == null) return 0;
        return article.date.compareTo(date);
    }
}
