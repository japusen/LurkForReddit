package com.example.lurkforreddit.data.repository

import android.util.Log
import com.example.lurkforreddit.data.remote.AccessTokenService
import com.example.lurkforreddit.domain.repository.AccessTokenRepository
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException

class DefaultAccessTokenRepository(
    private val accessTokenService: AccessTokenService
): AccessTokenRepository {

    private var accessTokenHeader: String = ""
    override suspend fun getAccessToken(): String {
        if (accessTokenHeader == "") {
            initAccessToken()
            return accessTokenHeader
        }
        return accessTokenHeader
    }

    private suspend fun initAccessToken() {
        // block network requests until token is initialized
        runBlocking {
            accessTokenHeader = try {
                val accessTokenDto = accessTokenService.getToken()
                "Bearer ${accessTokenDto.accessToken}"
            } catch (e: IOException) {
                Log.d("TOKEN", e.toString())
                ""
            } catch (e: HttpException) {
                Log.d("TOKEN", e.toString())
                ""
            }
        }
    }
}