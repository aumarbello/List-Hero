package com.aumarbello.motion.data

import java.io.Serializable

data class Link (
    val id: String,
    val thumbnail: String?,
    val title: String,
    val subreddit: String,
    val flair: String?,
    val isOriginalContent: Boolean,
    val domain: String,
    val votes: String,
    val comments: String
): Serializable