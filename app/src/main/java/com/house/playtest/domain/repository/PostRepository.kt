package com.house.playtest.domain.repository

import com.house.playtest.domain.model.Post

interface PostRepository {
    suspend fun getPosts(page: Int, pageSize: Int): List<Post>
    suspend fun getPostById(postId: String): Post?
    suspend fun toggleLike(postId: String): Post?
}
