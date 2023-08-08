package com.example.lurkforreddit.util

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Encode the url for use as a parameter in navigation
 * @param url
 * @return the encoded url
 */
fun encodedUrl(url: String): String {
    return URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
}