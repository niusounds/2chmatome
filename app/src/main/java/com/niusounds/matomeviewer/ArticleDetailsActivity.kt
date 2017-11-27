package com.niusounds.matomeviewer

import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.android.volley.Response
import com.niusounds.matomeviewer.data.Article
import com.niusounds.matomeviewer.data.DatabaseManager
import com.niusounds.matomeviewer.util.VolleyUtils
import kotlinx.android.synthetic.main.activity_article_details.*
import org.androidannotations.annotations.*
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import java.io.IOException

@EActivity
open class ArticleDetailsActivity : AppCompatActivity() {

    private var article: Article? = null

    @Extra
    lateinit var link: String

    @Bean
    lateinit var v: VolleyUtils
    @Bean
    lateinit var databaseManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)

        databaseManager.articleDao.findByUrl(link).observe(this, Observer<Article> { article ->
            this.article = article

            if (article != null) {
                toolbar.title = article.title

                // 短いコンテンツを一時表示している間に完全コンテンツを取得する
                applyHtml(article.contentEncoded)
                v.requestString(article.link, Response.Listener { html -> parse(html) }, Response.ErrorListener { finish() })
            }
        })

        // Init toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.inflateMenu(R.menu.menu_details)
        toolbar.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
    }

    @Background
    open fun parse(html: String) {
        var html = html
        val doc = Jsoup.parse(html)

        // #article-contents
        val content = doc.getElementById("article-contents")
        if (content != null) {

            content.select("iframe").remove()
            content.select(".link2, .article_mid, .no-pc, .linkh").remove()
            content.select("span[style=\"font-size: large;\"]").remove()
            content.select("span[style=\"color: rgb(255, 0, 255);\"]").remove()
            content.select("a[onclick]").remove()
            content.select("a[rel=nofollow]").remove()

            html = content.html()
            html = Jsoup.clean(html, Whitelist.simpleText()
                    .addTags("br")
                    .addAttributes("span", "style")
                    .addAttributes("div", "style")
                    .addAttributes("img", "src"))

            // apply template
            try {
                val template = resources.openRawResource(R.raw.template).bufferedReader().readText()
                html = template.replace("{{content}}", html)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        applyHtml(html)
        hideProgress()
    }

    @UiThread
    open fun applyHtml(html: String?) {
        webview.loadDataWithBaseURL("", html, "text/html", "UTF-8", "")
    }

    @UiThread
    open fun hideProgress() {
        progress.animate().translationY((-progress.height).toFloat()).withEndAction {
            progress.visibility = View.GONE
            text.setText(R.string.complete)
            hideText()
        }
    }

    @UiThread(delay = 500)
    open fun hideText() {
        text.animate().translationY(text.height.toFloat())
    }

    @OptionsItem
    fun menuOpenBrowser() {
        article?.let { article ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article.link)))
        }
    }

    @OptionsItem
    fun menuShare() {
        article?.let { article ->
            startActivity(
                    Intent.createChooser(
                            Intent(Intent.ACTION_SEND)
                                    .setType("text/plain")
                                    .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text, article.title, article.link)), getText(R.string.menu_share)))
        }
    }
}
