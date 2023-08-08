package com.example.lurkforreddit.util

import com.example.lurkforreddit.R

enum class ListingSort(val value: String) {
    HOT("hot"),
    RISING("rising"),
    NEW("new"),
    TOP("top")
}

enum class UserContentType(val value: String) {
    SUBMITTED("submitted"),
    COMMENTS("comments")
}

enum class UserListingSort(val value: String) {
    HOT("hot"),
    NEW("new"),
    TOP("top"),
    CONTROVERSIAL("controversial")
}

enum class TopSort(val value: String) {
    HOUR("hour"),
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    YEAR("year"),
    ALL("all")
}

enum class CommentSort(val value: String) {
    BEST("confidence"),
    TOP("top"),
    NEW("new"),
    CONTROVERSIAL("controversial"),
    QA("qa")
}

enum class DuplicatesSort(val value: String) {
    NUMCOMMENTS("num_comments"),
    NEW("new")
}

enum class PagingListing(val value: String) {
    POSTS("posts"),
    DUPLICATES("duplicates"),
    USERSUBMISSIONS("user_submissions"),
    USERCOMMENTS("user_comments")
}

enum class CommentSortItems(
    val sort: CommentSort,
    val text: String,
    val iconID: Int,
) {
    BEST(
        sort = CommentSort.BEST,
        text = "Best",
        iconID = R.drawable.ic_star
    ),
    TOP(
        sort = CommentSort.TOP,
        text = "Top",
        iconID = R.drawable.ic_rising
    ),
    NEW(
        sort = CommentSort.NEW,
        text = "New",
        iconID = R.drawable.ic_time
    ),
    CONTROVERSIAL(
        sort = CommentSort.CONTROVERSIAL,
        text = "Controversial",
        iconID = R.drawable.ic_controversial
    ),
    QA(
        sort = CommentSort.QA,
        text = "Q&A",
        iconID = R.drawable.ic_question_answer
    )
}