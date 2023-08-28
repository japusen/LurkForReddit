package com.example.lurkforreddit

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LurkApplication: Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(ImageDecoderDecoder.Factory())
                add(GifDecoder.Factory())
            }
            .build()
    }
}