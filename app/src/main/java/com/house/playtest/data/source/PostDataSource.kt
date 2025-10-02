package com.house.playtest.data.source

import com.house.playtest.data.model.PostEntity

interface PostDataSource {
    /**
     * Fetch a page of posts
     * @param page The page number (0-indexed)
     * @param pageSize Number of items per page
     * @return List of PostEntity
     */
    suspend fun getPosts(page: Int, pageSize: Int): List<PostEntity>
    
    /**
     * Get a single post by ID
     * @param postId The post ID
     * @return PostEntity or null if not found
     */
    suspend fun getPostById(postId: String): PostEntity?
    
    /**
     * Toggle like status for a post
     * @param postId The post ID
     * @return Updated PostEntity or null if not found
     */
    suspend fun toggleLike(postId: String): PostEntity?
}

