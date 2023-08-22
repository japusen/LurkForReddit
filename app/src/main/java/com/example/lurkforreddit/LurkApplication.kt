package com.example.lurkforreddit

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.lurkforreddit.di.AppContainer
import com.example.lurkforreddit.di.DefaultAppContainer

class LurkApplication: Application(), ImageLoaderFactory {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(ImageDecoderDecoder.Factory())
                add(GifDecoder.Factory())
            }
            .build()
    }
}