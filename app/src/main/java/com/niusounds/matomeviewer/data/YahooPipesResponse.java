package com.niusounds.matomeviewer.data;

import com.google.gson.annotations.SerializedName;

public class YahooPipesResponse {
    public int count;
    public Value value;

    public static class Value {
        public String title;
        public String description;
        public String link;
        public String pubDate;
        public String generator;
        public String callback;
        public Article[] items;
    }
}
