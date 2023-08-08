package com.example.lurkforreddit.util

import com.example.lurkforreddit.R

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

enum class DuplicatesSortItems(
    val sort: DuplicatesSort,
    val text: String,
    val iconID: Int,
) {
    NUMCOMMENTS(
        sort = DuplicatesSort.NUMCOMMENTS,
        text = "Number of Comments",
        iconID = R.drawable.ic_comment
    ),
    NEW(
        sort = DuplicatesSort.NEW,
        text = "New",
        iconID = R.drawable.ic_time
    )
}

enum class ListingSortItems(
    val sort: ListingSort,
    val text: String,
    val iconID: Int,
) {
    HOT(
        sort = ListingSort.HOT,
        text = "Hot",
        iconID = R.drawable.ic_hot
    ),
    RISING(
        sort = ListingSort.RISING,
        text = "Rising",
        iconID = R.drawable.ic_rising
    ),
    NEW(
        sort = ListingSort.NEW,
        text = "New",
        iconID = R.drawable.ic_time
    ),
    TOP(
        sort = ListingSort.TOP,
        text = "Top",
        iconID = R.drawable.ic_top
    )
}

enum class TopSortItems(
    val sort: TopSort,
    val text: String,
) {
    ALL(
        sort = TopSort.ALL,
        text = "All",
    ),
    YEAR(
        sort = TopSort.YEAR,
        text = "Year",
    ),
    MONTH(
        sort = TopSort.MONTH,
        text = "Month"
    ),
    WEEK(
        sort = TopSort.WEEK,
        text = "Week"
    ),
    DAY(
        sort = TopSort.DAY,
        text = "Day"
    ),
    HOUR(
        sort = TopSort.HOUR,
        text = "Hour"
    )
}

enum class UserContentTypeItems(
    val contentType: UserContentType,
    val text: String,
    val iconID: Int,
) {
    SUBMISSIONS(
        contentType = UserContentType.SUBMITTED,
        text = "Submissions",
        iconID = R.drawable.ic_submissions
    ),
    COMMENTS(
        contentType = UserContentType.COMMENTS,
        text = "Comments",
        iconID = R.drawable.ic_comment
    )
}

enum class UserListingSortItems(
    val sort: UserListingSort,
    val text: String,
    val iconID: Int
) {
    HOT(
        sort = UserListingSort.HOT,
        text = "Hot",
        iconID = R.drawable.ic_hot
    ),
    NEW(
        sort = UserListingSort.NEW,
        text = "New",
        iconID = R.drawable.ic_time
    ),
    TOP(
        sort = UserListingSort.TOP,
        text = "Top",
        iconID = R.drawable.ic_top
    ),
    CONTROVERSIAL(
        sort = UserListingSort.CONTROVERSIAL,
        text = "Controversial",
        iconID = R.drawable.ic_controversial
    )
}