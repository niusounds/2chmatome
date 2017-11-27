package com.niusounds.matomeviewer.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface ArticleDao {

    /**
     * 記事リストを取得する。
     */
    @Query("SELECT * FROM article ORDER BY pubDate DESC LIMIT :limit")
    fun getAll(limit: Int = 100): LiveData<List<Article>>

    /**
     * 記事を保存する。メインスレッドで呼んではいけない。
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(article: Article)
}