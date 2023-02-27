package com.os.operando.rebuildfm

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EpisodeDetailModule {

    @Provides
    @Singleton
    public fun provideRssFeedRepository(
        @ApplicationContext context: Context,
    ): RssFeedRepository {
        return RssFeedRepository(
            CacheStorage(context)
        )
    }
}