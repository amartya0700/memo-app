package com.publicprojects.memo.di

import com.publicprojects.memo.model.db.MemoDao
import com.publicprojects.memo.model.repository.MemoRepository
import com.publicprojects.memo.model.repository.MemoRepositoryImpl
import com.publicprojects.memo.util.Utils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideMemoRepository(
        memoDao: MemoDao,
        @Named(Utils.CONFLICT_EXCEPTION) excMsg: String
    ): MemoRepository =
        MemoRepositoryImpl(memoDao = memoDao, conflictExceptionMessage = excMsg)
}