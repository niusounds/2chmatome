package com.niusounds.matomeviewer

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.niusounds.matomeviewer.data.Article
import com.niusounds.matomeviewer.data.DatabaseManager
import com.niusounds.matomeviewer.event.ArticleClickEvent
import com.niusounds.matomeviewer.util.DividerItemDecoration
import de.greenrobot.event.EventBus
import kotlinx.android.synthetic.main.activity_main.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity
open class MainActivity : AppCompatActivity() {

    @Bean
    lateinit var databaseManager: DatabaseManager
    @Bean
    lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init list
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        list.setHasFixedSize(true)
        list.itemAnimator = DefaultItemAnimator()
        list.addItemDecoration(DividerItemDecoration(this))

        // Init adapter
        val adapter = ArticleAdapter()
        list.adapter = adapter

        // データベースの変更をUIに反映
        databaseManager.articleDao.getAll().observe(this, Observer<List<Article>> { articles ->
            if (articles != null) {
                adapter.articles = articles
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    @AfterViews
    fun reload() {
        api.loadAll()
    }

    /**
     * Notified when tapped on [ItemView].
     */
    fun onEvent(e: ArticleClickEvent) {
        ArticleDetailsActivity_.intent(this)
                .link(e.article.link)
                .start()
    }
}
