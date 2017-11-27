package com.niusounds.matomeviewer

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.niusounds.matomeviewer.data.Article
import com.niusounds.matomeviewer.data.DatabaseManager
import com.niusounds.matomeviewer.event.ArticleClickEvent
import com.niusounds.matomeviewer.util.DividerItemDecoration
import de.greenrobot.event.EventBus
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity(R.layout.activity_main)
open class MainActivity : AppCompatActivity() {

    @Bean
    lateinit var databaseManager: DatabaseManager

    @ViewById
    lateinit var toolbar: Toolbar
    @ViewById
    lateinit var list: RecyclerView

    @Bean
    lateinit var adapter: ArticleAdapter
    @Bean
    lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    fun initToolbar() {
        toolbar.setTitle(R.string.articles)
    }

    @AfterViews
    fun initList() {
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        list.setHasFixedSize(true)
        list.itemAnimator = DefaultItemAnimator()
        list.addItemDecoration(DividerItemDecoration(this))
        list.adapter = adapter
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
                .article(e.article)
                .start()
    }
}
