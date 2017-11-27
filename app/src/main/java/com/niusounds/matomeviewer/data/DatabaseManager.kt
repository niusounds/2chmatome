package com.niusounds.matomeviewer.data

import android.arch.persistence.room.Room
import android.content.Context
import org.androidannotations.annotations.AfterInject
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext

/**
 * DAOの参照を保持するシングルトンクラス。
 */
@EBean(scope = EBean.Scope.Singleton)
open class DatabaseManager {

    private lateinit var db: AppDatabase

    @RootContext
    lateinit var context: Context

    val articleDao: ArticleDao
        get() = db.articleDao

    @AfterInject
    fun init() {
        db = Room.databaseBuilder(context, AppDatabase::class.java, "article-database").build()
    }
}