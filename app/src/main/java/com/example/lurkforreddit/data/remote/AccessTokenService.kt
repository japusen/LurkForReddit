package com.example.lurkforreddit.data.remote

import com.example.lurkforreddit.BuildConfig
import com.example.lurkforreddit.data.remote.model.AccessTokenDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.Base64

interface AccessTokenService {
    @FormUrlEncoded
    @POST("/api/v1/access_token")
    suspend fun fetchAccessToken(
        @Header("Authorization") credentials: String = encodedCredentials(),
        @Field("grant_type") grantType: String = GRANT_TYPE,
        @Field("device_id") deviceId: String = DEVICE_ID
    ): AccessTokenDto

    companion object {
        private const val GRANT_TYPE = "https://oauth.reddit.com/grants/installed_client"
        private const val DEVICE_ID = "DO_NOT_TRACK_THIS_DEVICE"
        private const val CLIENT_ID = BuildConfig.REDDIT_CLIENT_ID
        private const val PW = ""
        private const val CREDENTIALS = "$CLIENT_ID:$PW"

        private fun encodedCredentials() : String {
            val encodedID = Base64.getEncoder().encodeToString(CREDENTIALS.toByteArray())
            return "Basic $encodedID"
        }
    }
}
