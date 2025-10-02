package com.house.playtest.domain.usecase

import com.house.playtest.domain.model.Post
import com.house.playtest.domain.repository.PostRepository
import javax.inject.Inject

class ToggleLikeUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String): Result<Post> {
        return try {
            val updatedPost = repository.toggleLike(postId)
            if (updatedPost != null) {
                Result.success(updatedPost)
            } else {
                Result.failure(Exception("Post not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
