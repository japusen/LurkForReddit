package com.example.lurkforreddit.util

enum class ListingSort(val type: String) {
    HOT("hot"),
    RISING("rising"),
    NEW("new"),
    TOP("top")
}

enum class UserListing(val type: String) {
    SUBMITTED("submitted"),
    COMMENTS("comments")
}

enum class UserSort(val type: String) {
    HOT("hot"),
    NEW("new"),
    TOP("top"),
}

enum class TopSort(val type: String) {
    HOUR("hour"),
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    YEAR("year"),
    ALL("all")
}

enum class CommentSort(val type: String) {
    BEST("confidence"),
    TOP("top"),
    NEW("new"),
    CONTROVERSIAL("controversial"),
    QA("qa")
}