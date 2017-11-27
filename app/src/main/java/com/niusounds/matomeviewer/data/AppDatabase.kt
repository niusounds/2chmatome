package com.niusounds.matomeviewer.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by niuso on 2017/11/27.
 */
@Database(entities = arrayOf(Article::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val articleDao: ArticleDao
}