package com.niusounds.matomeviewer;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.niusounds.matomeviewer.data.Article;
import com.niusounds.matomeviewer.util.VolleyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

@EActivity(R.layout.activity_article_details)
public class ArticleDetailsActivity extends ActionBarActivity {
    @Extra
    Article article;

    @ViewById
    Toolbar toolbar;
    @ViewById
    WebView webview;
    @ViewById
    ProgressBar progress;
    @ViewById
    TextView text;

    @Bean
    VolleyUtils v;

    @AfterViews
    void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(article.title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @AfterViews
    void loadContent() {
        // 短いコンテンツを一時表示して完全コンテンツを取得する
        applyHtml(article.contentEncoded);
        v.requestString(article.link, new Response.Listener<String>() {
            @Override
            public void onResponse(String html) {
                parse(html);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
            }
        });
    }

    @Background
    void parse(String html) {
        Document doc = Jsoup.parse(html);

        // #article-contents
        Element content = doc.getElementById("article-contents");
        if (content != null) {
            html = Jsoup.clean(content.html(), Whitelist.simpleText()
                    .addTags("br")
                    .addAttributes("span", "style")
                    .addAttributes("div", "style")
                    .addAttributes("img", "src"));

            // apply template
            try {
                String template = IOUtils.toString(getResources().openRawResource(R.raw.template), "UTF-8");
                html = template.replace("{{content}}", html);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        applyHtml(html);
        hideProgress();
    }

    @UiThread
    void applyHtml(String html) {
        webview.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
    }

    @UiThread
    void hideProgress() {
        progress.animate().translationY(-progress.getHeight()).withEndAction(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.GONE);
                text.setText(R.string.complete);
                hideText();
            }
        });
    }

    @UiThread(delay = 500)
    void hideText() {
        text.animate().translationY(text.getHeight());
    }

    @Override
    @OptionsItem(android.R.id.home)
    public void onBackPressed() {
        super.onBackPressed();
    }
}
