package com.example.lurkforreddit

import android.app.Application
import com.example.lurkforreddit.data.AppContainer
import com.example.lurkforreddit.data.DefaultAppContainer

class LurkApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}