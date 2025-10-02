package com.house.playtest.data.source

import com.house.playtest.data.model.PostEntity
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class MockPostDataSource @Inject constructor() : PostDataSource {
    
    // Predefined image URLs from Lorem Picsum
    private val imageUrls = listOf(
        "https://picsum.photos/id/1/800/600",
        "https://picsum.photos/id/10/800/600",
        "https://picsum.photos/id/20/800/600",
        "https://picsum.photos/id/30/800/600",
        "https://picsum.photos/id/40/800/600",
        "https://picsum.photos/id/50/800/600",
        "https://picsum.photos/id/60/800/600",
        "https://picsum.photos/id/70/800/600",
        "https://picsum.photos/id/80/800/600",
        "https://picsum.photos/id/100/800/600"
    )
    
    // Random caption templates
    private val captionTemplates = listOf(
        "Beautiful sunset at the beach 🌅",
        "Amazing view from the mountains! ⛰️",
        "Just another day in paradise",
        "Living my best life ✨",
        "Nature never goes out of style 🌿",
        "Making memories one photo at a time",
        "Adventure awaits! 🚀",
        "Exploring new places",
        "Grateful for moments like these 🙏",
        "Chasing dreams and sunsets",
        "Life is short, make it sweet 🍭",
        "Weekend vibes only",
        "Finding beauty in simple things",
        "Taking it one day at a time",
        "Coffee and contemplation ☕"
    )
    
    private val authorNames = listOf(
        "Alex Johnson", "Maria Garcia", "John Smith", "Emma Wilson",
        "Michael Brown", "Sophia Davis", "James Miller", "Olivia Martinez",
        "William Anderson", "Isabella Taylor", "David Thomas", "Mia Jackson"
    )
    
    // In-memory storage for posts
    private val postsCache = mutableMapOf<String, PostEntity>()
    
    // Keep track of generated posts to maintain consistency
    private var totalGeneratedPosts = 0
    private val maxPosts = 100 // Limit total posts for demo
    
    override suspend fun getPosts(page: Int, pageSize: Int): List<PostEntity> {
        // Simulate network delay
        delay(500)
        
        val startIndex = page * pageSize
        val posts = mutableListOf<PostEntity>()
        
        for (i in startIndex until startIndex + pageSize) {
            if (i >= maxPosts) break
            
            val postId = "post_$i"
            
            // Return from cache if exists, otherwise generate new
            val post = postsCache.getOrPut(postId) {
                generatePost(postId, i)
            }
            
            posts.add(post)
        }
        
        return posts
    }
    
    override suspend fun getPostById(postId: String): PostEntity? {
        delay(200) // Simulate network delay
        return postsCache[postId]
    }
    
    override suspend fun toggleLike(postId: String): PostEntity? {
        delay(300) // Simulate network delay
        
        val post = postsCache[postId] ?: return null
        
        val updatedPost = post.copy(
            isLiked = !post.isLiked,
            likeCount = if (post.isLiked) post.likeCount - 1 else post.likeCount + 1
        )
        
        postsCache[postId] = updatedPost
        return updatedPost
    }
    
    private fun generatePost(postId: String, index: Int): PostEntity {
        val random = Random(index) // Use index as seed for consistency
        
        return PostEntity(
            id = postId,
            imageUrl = imageUrls[index % imageUrls.size],
            caption = captionTemplates[random.nextInt(captionTemplates.size)],
            authorName = authorNames[random.nextInt(authorNames.size)],
            likeCount = random.nextInt(50, 5000),
            isLiked = false,
            timestamp = System.currentTimeMillis() - (index * 3600000L) // 1 hour ago per post
        )
    }
}

