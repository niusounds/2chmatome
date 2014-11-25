package com.niusounds.matomeviewer;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.niusounds.matomeviewer.data.Article;
import com.niusounds.matomeviewer.event.ArticleClickEvent;
import com.niusounds.matomeviewer.event.NewArticleEvent;
import com.niusounds.matomeviewer.util.DividerItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {
    private List<Article> articles = new ArrayList<Article>();

    @ViewById
    Toolbar toolbar;
    @ViewById
    RecyclerView list;

    @Bean
    ArticleAdapter adapter;
    @Bean
    Api api;

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @AfterViews
    void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.articles);
    }

    @AfterViews
    void initList() {
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
        list.addItemDecoration(new DividerItemDecoration(this));
        list.setAdapter(adapter);
        adapter.setArticles(articles);
    }

    @AfterViews
    void reload() {
        api.loadAll();
    }

    public void onEvent(ArticleClickEvent e) {
        ArticleDetailsActivity_.intent(this).article(e.article).start();
    }

    public void onEvent(NewArticleEvent e) {
        articles.add(e.article);
        Collections.sort(articles);
        adapter.notifyDataSetChanged();
    }
}
