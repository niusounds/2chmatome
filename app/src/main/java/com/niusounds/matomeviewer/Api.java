package com.niusounds.matomeviewer;

import com.niusounds.matomeviewer.data.Article;
import com.niusounds.matomeviewer.event.NewArticleEvent;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.greenrobot.event.EventBus;

@EBean
public class Api {

    //    private static final String URL_MATOME = "https://pipes.yahoo.com/pipes/pipe.run?_id=5e4b0f39af93d30a0d9c2b29bca588ea&_render=json";
    //    private static final String URL_NEWSOKU = "http://pipes.yahoo.com/pipes/pipe.run?_id=f74242166c519e8664aedb178517efbc&_render=json";
    //    private static final String URL_HAMUSOKU = "http://pipes.yahoo.com/pipes/pipe.run?_id=e9f04da946810163052385717f445ace&_render=json";

    private static final String URL_MATOME = "http://blog.livedoor.jp/dqnplus/index.rdf";
//    private static final String URL_NEWSOKU = "http://new-soku.net/rss/index_new_img.rdf";

    private EventBus eventBus = EventBus.getDefault();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");

    @Background
    public void loadAll() {
        parse(URL_MATOME);
//        parse(URL_NEWSOKU);
//        v.request(URL_NEWSOKU, YahooPipesResponse.class, this, this);
//        v.request(URL_HAMUSOKU, YahooPipesResponse.class, this, this);
    }

    private void parse(String url) {
        try {
            org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url);
            final NodeList items = document.getElementsByTagName("item");
            final int length = items.getLength();
            List<Article> articles = new ArrayList<>(length);
            for (int i = 0; i < length; ++i) {
                Node node = items.item(i);

                Article article = new Article();
                Node child = node.getFirstChild();

                while (child != node.getLastChild()) {
                    switch (child.getNodeName()) {
                        case "title":
                            article.title = child.getTextContent();
                            break;
                        case "content:encoded":
                            article.contentEncoded = child.getTextContent();
                            break;
                        case "description":
                            article.description = child.getTextContent();
                            break;
                        case "link":
                            article.link = child.getTextContent();
                            break;
                        case "dc:date":
                            article.pubDate = child.getTextContent();
                            break;
                    }
                    child = child.getNextSibling();
                }

                articles.add(article);
            }

            processArticles(articles);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
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
}
