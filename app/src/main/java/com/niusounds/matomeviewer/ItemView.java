package com.niusounds.matomeviewer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.niusounds.matomeviewer.data.Article;
import com.niusounds.matomeviewer.event.ArticleClickEvent;
import com.niusounds.matomeviewer.util.VolleyUtils;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;

import de.greenrobot.event.EventBus;

@EViewGroup(R.layout.item)
public class ItemView extends FrameLayout implements View.OnClickListener {
    private Article article;
    @ViewById
    NetworkImageView image;
    @ViewById
    TextView title;

    @Bean
    VolleyUtils v;

    @DimensionPixelSizeRes
    int articleListPadding;

    public ItemView(Context context) {
        super(context);
        setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        setOnClickListener(this);
    }

    @AfterInject
    void init() {
        setPadding(0, articleListPadding, 0, articleListPadding);
    }

    public void bind(Article article) {
        this.article = article;
        title.setText(article.title);
        image.setImageUrl(article.imageUrl, v.getDefaultImageLoader());
    }

    @Override
    public void onClick(View view) {
        EventBus.getDefault().post(new ArticleClickEvent(article));
    }
}
