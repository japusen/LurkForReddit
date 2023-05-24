package com.example.lurkforreddit.network

import android.util.Log
import com.example.lurkforreddit.model.AccessToken
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.Base64

private const val BASE_URL = "https://www.reddit.com"
private const val GRANT_TYPE = "https://oauth.reddit.com/grants/installed_client"
private const val DEVICE_ID = "DO_NOT_TRACK_THIS_DEVICE"
private const val CLIENT_ID = "6ibP_L7dIcbjgtlCjgLacA"
private const val PASSWORD = ""
private const val CREDENTIALS = "$CLIENT_ID:$PASSWORD"

private fun encodedCredentials() : String {
    val encodedID = Base64.getEncoder().encodeToString(CREDENTIALS.toByteArray())
    return "Basic $encodedID"
}

interface AccessTokenService {
    @FormUrlEncoded
    @POST("/api/v1/access_token")
    suspend fun getToken(
        @Header("Authorization") credentials: String = encodedCredentials(),
        @Field("grant_type") grantType: String = GRANT_TYPE,
        @Field("device_id") deviceId: String = DEVICE_ID
    ): Response<AccessToken>
}

private val json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

object TokenApi {
    private val retrofitService: AccessTokenService by lazy {
        retrofit.create(AccessTokenService::class.java)
    }

    suspend fun post(): AccessToken = withContext(Dispatchers.IO) {
        val response = retrofitService.getToken()
        if (response.isSuccessful) {
            Log.d("AccessToken", "Response:  ${response.body()}")
            return@withContext response.body()!!
        } else {
            throw Exception(response.errorBody()?.charStream()?.readText())
        }
    }
}

