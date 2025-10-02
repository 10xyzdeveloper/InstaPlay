package com.house.playtest.domain.model

data class Post(
    val id: String,
    val imageUrl: String,
    val caption: String,
    val authorName: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val timestamp: Long
)

