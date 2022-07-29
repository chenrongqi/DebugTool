package com.crqi.deeplink

import android.app.Application
import android.graphics.Bitmap
import com.crqi.deeplink.util.MMkvUtils
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.memory.MemoryTrimType
import com.facebook.common.memory.MemoryTrimmableRegistry
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImageTranscoderType
import com.facebook.imagepipeline.core.MemoryChunkType
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class LinkApplication :Application(){
    override fun onCreate() {
        MMkvUtils.context = this;
        super.onCreate()
        val builder: ImagePipelineConfig.Builder = createFrescoConfig(this)

        builder.setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
            .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
        builder.experiment().setNativeCodeDisabled(true)
        Fresco.initialize(this)
    }
}

private fun createFrescoConfig(context: Application): ImagePipelineConfig.Builder {

    val mainDiskCacheConfig = DiskCacheConfig.newBuilder(context)
        .setBaseDirectoryPath(context.cacheDir)
        .setBaseDirectoryName("fresco cache")
        .setMaxCacheSize(80 * 1024 * 1024.toLong())
        .setMaxCacheSizeOnLowDiskSpace(20 * 1024 * 1024.toLong())
        .setMaxCacheSizeOnVeryLowDiskSpace(10 * 1024 * 1024.toLong())
        .setVersion(2)

        .build()
    val smallDiskCacheConfig = DiskCacheConfig.newBuilder(context)
        .setBaseDirectoryPath(context.cacheDir)
        .setBaseDirectoryName("fresco small cache")
        .setMaxCacheSize(10 * 1024 * 1024.toLong())
        .setMaxCacheSizeOnLowDiskSpace(5 * 1024 * 1024.toLong())
        .setVersion(2)
        .build()
    val memoryTrimmableRegistry: MemoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance()
    memoryTrimmableRegistry.registerMemoryTrimmable { trimType: MemoryTrimType ->
        val suggestedTrimRatio = trimType.suggestedTrimRatio

    }
    val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
    val client: OkHttpClient = builder
        .build()
    return OkHttpImagePipelineConfigFactory.newBuilder(context, client)
        .setMainDiskCacheConfig(mainDiskCacheConfig)
        .setDownsampleEnabled(true)
        .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
        .setBitmapsConfig(Bitmap.Config.RGB_565)
        .setSmallImageDiskCacheConfig(smallDiskCacheConfig)
}