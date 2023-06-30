package com.example.lurkforreddit.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openLinkInBrowser(
    context: Context,
    url: String
) {
    val modifiedUrl = if (!url.startsWith("https://") && !url.startsWith("http://")){
        "http://$url";
    } else {
        url
    }
    val webpage: Uri = Uri.parse(modifiedUrl)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}