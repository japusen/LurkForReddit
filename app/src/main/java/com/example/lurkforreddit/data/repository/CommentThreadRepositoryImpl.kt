package com.example.lurkforreddit.data.repository

import com.example.lurkforreddit.data.json.parseMoreComments
import com.example.lurkforreddit.data.json.parsePostComments
import com.example.lurkforreddit.data.json.parsePostListing
import com.example.lurkforreddit.data.remote.RedditApiService
import com.example.lurkforreddit.domain.model.Comment
import com.example.lurkforreddit.domain.model.CommentSort
import com.example.lurkforreddit.domain.model.CommentThreadItem
import com.example.lurkforreddit.domain.model.More
import com.example.lurkforreddit.domain.model.Post
import com.example.lurkforreddit.domain.repository.AccessTokenRepository
import com.example.lurkforreddit.domain.repository.CommentThreadRepository
import kotlinx.serialization.json.jsonArray

class CommentThreadRepositoryImpl(
    private val accessTokenRepository: AccessTokenRepository,
    private val redditApiService: RedditApiService
): CommentThreadRepository {

    private val maxNumOfComments = 50

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
    ): Pair<Post, List<CommentThreadItem>> {

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
        val commentThread = mutableListOf<CommentThreadItem>()
        parsePostComments(commentThreadJson, commentThread)

        return Pair(post, commentThread.toList())
    }

    /**
     * Network call to fetch more comments
     * @param linkID the id of the post
     * @param sort the type of sort (best, top, new, controversial, q&a)
     * @return a list of comments requested
     */
    override suspend fun addComments(
        commentThread: MutableList<CommentThreadItem>,
        index: Int,
        linkID: String,
        sort: CommentSort
    ): List<CommentThreadItem> {

        val tokenHeader = accessTokenRepository.getAccessToken()

        val more = commentThread[index] as More
        val ids = more.getIDs(maxNumOfComments)

        val response = redditApiService.fetchMoreComments(
            tokenHeader,
            "t3_$linkID",
            ids,
            sort.value
        )

        val newComments = mutableListOf<CommentThreadItem>()
        parseMoreComments(response, newComments)

        if (more.children.isEmpty())
            commentThread.removeAt(index)

        commentThread.addAll(index, newComments)

        return commentThread.toList()
    }

    override suspend fun changeCommentVisibility(
        commentThread: MutableList<CommentThreadItem>,
        show: Boolean,
        index: Int,
        depth: Int
    ): List<CommentThreadItem> {

        var start = index + 1
        while (start < commentThread.size) {
            val item = commentThread.elementAt(start)
            if (item.depth > depth) {
                when (item) {
                    is Comment -> commentThread[start] = item.copy(visible = show)
                    is More -> commentThread[start] = item.copy(visible = show)
                }
                start += 1
            }
            else
                break
        }

        return commentThread.toList()
    }
}