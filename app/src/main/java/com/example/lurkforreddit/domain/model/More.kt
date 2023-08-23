package com.example.lurkforreddit.domain.model

data class More(
    override val visible: Boolean,
    override val depth: Int,
    var children: List<String>
): CommentThreadItem {
    fun getIDs(count: Int): String {
        val ids: String
        if (children.size > count) {
            ids = children.subList(0, count).joinToString(",")
            children = children.subList(count, children.size)
        } else {
            ids = children.joinToString(",")
            children = listOf()
        }
        return ids
    }
}