package com.niusounds.matomeviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
class Article : Serializable, Comparable<Article> {
    @PrimaryKey
    var link: String = ""
    var title: String? = null
    var imageUrl: String? = null
    var description: String? = null
    @Ignore
    var date: Date? = null
    var pubDate: String? = null
    var contentEncoded: String? = null

    override fun compareTo(other: Article): Int =
            if (other.date == null || date == null) 0
            else other.date!!.compareTo(date)
}
