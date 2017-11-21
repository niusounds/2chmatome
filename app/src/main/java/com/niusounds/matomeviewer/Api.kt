package com.niusounds.matomeviewer

import com.niusounds.matomeviewer.data.Article
import com.niusounds.matomeviewer.event.NewArticleEvent
import de.greenrobot.event.EventBus
import org.androidannotations.annotations.Background
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.UiThread
import org.androidannotations.annotations.res.StringArrayRes
import org.jsoup.Jsoup
import org.w3c.dom.Node
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

@EBean
open class Api {

    companion object {
        val documentBuilderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    }

    private val eventBus = EventBus.getDefault()
    private val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US)

    @StringArrayRes
    lateinit var urls: Array<String>

    fun loadAll() {
        urls.forEach { url -> parse(url) }
    }

    @Background
    open fun parse(url: String) {
        try {

            val document = documentBuilderFactory.newDocumentBuilder().parse(url)
            val items = document.getElementsByTagName("item")

            for (i in 0 until items.length) {
                val node = items.item(i)

                val article = parseArticle(node)
                processArticle(article)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun parseArticle(node: Node): Article {

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

        return article
    }

    private fun processArticle(article: Article) {

        val doc = Jsoup.parse(article.contentEncoded)
        article.imageUrl = doc.getElementsByTag("img").attr("src")
        try {
            article.date = format.parse(article.pubDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        postArticle(article)
    }

    @UiThread
    open fun postArticle(article: Article) {
        eventBus.post(NewArticleEvent(article))
    }
}
