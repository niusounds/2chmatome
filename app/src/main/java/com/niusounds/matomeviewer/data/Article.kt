package com.niusounds.matomeviewer.data

import java.io.Serializable
import java.util.*

class Article : Serializable, Comparable<Article> {
    var link: String? = null
    var title: String? = null
    var imageUrl: String? = null
    var description: String? = null
    var date: Date? = null
    var pubDate: String? = null
    var contentEncoded: String? = null

    override fun compareTo(other: Article): Int =
            if (other.date == null || date == null) 0
            else other.date!!.compareTo(date)
}
