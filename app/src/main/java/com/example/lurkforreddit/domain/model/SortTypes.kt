package com.example.lurkforreddit.domain.model

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

