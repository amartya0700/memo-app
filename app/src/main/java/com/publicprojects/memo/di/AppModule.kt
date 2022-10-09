package com.publicprojects.memo.di

import android.content.Context
import com.publicprojects.memo.R
import com.publicprojects.memo.util.Utils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named(Utils.CONFLICT_EXCEPTION)
    fun provideConflictExceptionMessage(@ApplicationContext context: Context) =
        context.getString(R.string.conflict_exception)
}