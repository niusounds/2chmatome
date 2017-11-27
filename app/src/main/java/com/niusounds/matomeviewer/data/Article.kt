package com.niusounds.matomeviewer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Article {
    @PrimaryKey
    var link: String = ""
    var title: String? = null
    var imageUrl: String? = null
    var description: String? = null
    var pubDate: String? = null
    var contentEncoded: String? = null
}
