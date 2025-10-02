package com.house.playtest.data.model

data class PostEntity(
    val id: String,
    val imageUrl: String,
    val caption: String,
    val authorName: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val timestamp: Long
)

