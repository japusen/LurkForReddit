package com.example.lurkforreddit.data.repository

import com.example.lurkforreddit.data.json.parseMoreComments
import com.example.lurkforreddit.data.json.parsePostComments
import com.example.lurkforreddit.data.json.parsePostListing
import com.example.lurkforreddit.data.remote.RedditApiService
import com.example.lurkforreddit.domain.model.CommentSort
import com.example.lurkforreddit.domain.model.CommentThreadItem
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.repository.AccessTokenRepository
import com.example.lurkforreddit.domain.repository.CommentThreadRepository
import kotlinx.serialization.json.jsonArray

class CommentThreadRepositoryImpl(
    private val accessTokenRepository: AccessTokenRepository,
    private val redditApiService: RedditApiService
): CommentThreadRepository {

    /**
     * Network call to fetch comments of a specific post
     * @param subreddit the name of the subreddit
     * @param article the id of the article (post)
     * @param sort the type of sort (number of comments, new)
     * @return Post and list of Comments
     */
    override suspend fun getCommentThread(
        subreddit: String,
        article: String,
        sort: CommentSort
    ): Pair<Post, MutableList<CommentThreadItem>> {

        val tokenHeader = accessTokenRepository.getAccessToken()

        val response = redditApiService.fetchCommentThread(
            tokenHeader,
            subreddit,
            article,
            sort.value
        )

        val postListingJson = response.jsonArray[0]
        val postListing = parsePostListing(postListingJson)
        // the post is in an one-item list
        val post = postListing.children[0]

        val commentThreadJson = response.jsonArray[1]
        val commentThread: MutableList<CommentThreadItem> = mutableListOf()
        parsePostComments(commentThreadJson, commentThread)

        return Pair(post, commentThread)
    }

    /**
     * Network call to fetch more comments
     * @param linkID the id of the post
     * @param sort the type of sort (best, top, new, controversial, q&a)
     * @return a list of comments requested
     */
    override suspend fun addComments(
        index: Int,
        linkID: String,
        ids: String,
        sort: CommentSort
    ): MutableList<CommentThreadItem> {

        val tokenHeader = accessTokenRepository.getAccessToken()

        val response = redditApiService.fetchMoreComments(
            tokenHeader,
            "t3_$linkID",
            ids,
            sort.value
        )

        val newComments = mutableListOf<CommentThreadItem>()
        parseMoreComments(response, newComments)

        return newComments
    }
}