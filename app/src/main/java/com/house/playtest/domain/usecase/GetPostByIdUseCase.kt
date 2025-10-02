package com.house.playtest.domain.usecase

import com.house.playtest.domain.model.Post
import com.house.playtest.domain.repository.PostRepository
import javax.inject.Inject

class GetPostByIdUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String): Post? {
        return repository.getPostById(postId)
    }
}
