package com.example.lurkforreddit.util

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Opens link in a browser intent and modifies the url if needed
 * @param context to launch an intent
 * @param url url to open
 */
fun openLinkInBrowser(
    context: Context,
    url: String,
    domain: String
) {
    var modifiedUrl = if (!url.startsWith("https://") && !url.startsWith("http://")){
        "https://$url";
    } else {
        url
    }

    if (domain == "v.redd.it" && modifiedUrl.contains("DASH")) {
        val index = modifiedUrl.indexOf("DASH")
        modifiedUrl = modifiedUrl.substring(0, index)
    }

    val webpage: Uri = Uri.parse(modifiedUrl)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}