package com.example.lurkforreddit.data.mappers

import com.example.lurkforreddit.data.remote.model.MoreDto
import com.example.lurkforreddit.domain.model.More

fun MoreDto.toMore(): More {
    return More(
        visible = true,
        depth = depth,
        children = children
    )
}