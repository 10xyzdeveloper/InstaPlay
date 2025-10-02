package com.house.playtest.data.repository

import com.house.playtest.data.model.PostEntity
import com.house.playtest.data.source.PostDataSource
import com.house.playtest.domain.model.Post
import com.house.playtest.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val dataSource: PostDataSource
) : PostRepository {
    
    override suspend fun getPosts(page: Int, pageSize: Int): List<Post> {
        return dataSource.getPosts(page, pageSize).map { it.toDomainModel() }
    }
    
    override suspend fun getPostById(postId: String): Post? {
        return dataSource.getPostById(postId)?.toDomainModel()
    }
    
    override suspend fun toggleLike(postId: String): Post? {
        return dataSource.toggleLike(postId)?.toDomainModel()
    }
    
    // Mapper function: Entity -> Domain
    private fun PostEntity.toDomainModel(): Post {
        return Post(
            id = id,
            imageUrl = imageUrl,
            caption = caption,
            authorName = authorName,
            likeCount = likeCount,
            isLiked = isLiked,
            timestamp = timestamp
        )
    }
}
