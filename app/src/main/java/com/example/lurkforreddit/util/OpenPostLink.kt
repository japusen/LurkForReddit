package com.example.lurkforreddit.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.NavController
import java.net.URI

/**
 * Determines whether to launch an intent or navigate to specific route based on the link url
 * @param context to launch an intent
 * @param navController to navigate to routes
 * @param url post url
 */
fun openPostLink(
    context: Context,
    navController: NavController,
    url: String
) {
    val uri = URI(url)
    val host = uri.host

    if (host.equals("i.redd.it"))
        navController.navigate("image/${encodedUrl(url)}")
    else if (host.equals("v.redd.it"))
        navController.navigate("video/${encodedUrl(url)}")
    else {
        val intent = CustomTabsIntent
            .Builder()
            .build()
        intent.launchUrl(context, Uri.parse(url))
    }
}