package com.niusounds.matomeviewer;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.niusounds.matomeviewer.data.Article;
import com.niusounds.matomeviewer.data.YahooPipesResponse;
import com.niusounds.matomeviewer.event.NewArticleEvent;
import com.niusounds.matomeviewer.util.VolleyUtils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

@EBean
public class Api implements Response.Listener<YahooPipesResponse>, Response.ErrorListener {
    private static final String URL_MATOME = "https://pipes.yahoo.com/pipes/pipe.run?_id=5e4b0f39af93d30a0d9c2b29bca588ea&_render=json";
    private static final String URL_NEWSOKU = "http://pipes.yahoo.com/pipes/pipe.run?_id=f74242166c519e8664aedb178517efbc&_render=json";
    private static final String URL_HAMUSOKU = "http://pipes.yahoo.com/pipes/pipe.run?_id=e9f04da946810163052385717f445ace&_render=json";

    private EventBus eventBus = EventBus.getDefault();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");

    @Bean
    VolleyUtils v;

    public void loadAll() {
        v.request(URL_MATOME, YahooPipesResponse.class, this, this);
        v.request(URL_NEWSOKU, YahooPipesResponse.class, this, this);
        v.request(URL_HAMUSOKU, YahooPipesResponse.class, this, this);
    }

    @Override
    public void onResponse(YahooPipesResponse response) {
        processArticles(Arrays.asList(response.value.items));
    }

    @Background
    void processArticles(List<Article> articles) {
        for (Article article : articles) {
            Document doc = Jsoup.parse(article.contentEncoded);
            article.imageUrl = doc.getElementsByTag("img").attr("src");
            try {
                article.date = format.parse(article.pubDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            postArticle(article);
        }
    }

    @UiThread
    void postArticle(Article article) {
        eventBus.post(new NewArticleEvent(article));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
