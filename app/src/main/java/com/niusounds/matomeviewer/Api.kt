package com.niusounds.matomeviewer

import com.niusounds.matomeviewer.data.Article
import com.niusounds.matomeviewer.event.NewArticleEvent
import de.greenrobot.event.EventBus
import org.androidannotations.annotations.Background
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.UiThread
import org.androidannotations.annotations.res.StringArrayRes
import org.jsoup.Jsoup
import org.xml.sax.SAXException
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

@EBean
open class Api {

    private val eventBus = EventBus.getDefault()
    private val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US)

    @StringArrayRes
    lateinit var urls: Array<String>

    @Background
    open fun loadAll() {
        for (url in urls) {
            parse(url)
        }
    }

    private fun parse(url: String) {
        try {
            val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)
            val items = document.getElementsByTagName("item")
            val length = items.length
            val articles = ArrayList<Article>(length)
            for (i in 0 until length) {
                val node = items.item(i)

                val article = Article()
                var child = node.firstChild

                while (child !== node.lastChild) {
                    when (child.nodeName) {
                        "title" -> article.title = child.textContent
                        "content:encoded" -> article.contentEncoded = child.textContent
                        "description" -> article.description = child.textContent
                        "link" -> article.link = child.textContent
                        "dc:date" -> article.pubDate = child.textContent
                    }
                    child = child.nextSibling
                }

                articles.add(article)
            }

            processArticles(articles)
        } catch (e: SAXException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        }

    }

    @Background
    open fun processArticles(articles: List<Article>) {
        for (article in articles) {
            val doc = Jsoup.parse(article.contentEncoded)
            article.imageUrl = doc.getElementsByTag("img").attr("src")
            try {
                article.date = format.parse(article.pubDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            postArticle(article)
        }
    }

    @UiThread
    open fun postArticle(article: Article) {
        eventBus.post(NewArticleEvent(article))
    }
}
