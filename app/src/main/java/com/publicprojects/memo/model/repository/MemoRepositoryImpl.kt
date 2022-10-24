package com.publicprojects.memo.model.repository

import com.publicprojects.memo.model.Memo
import com.publicprojects.memo.model.db.MemoDao
import com.publicprojects.memo.util.CustomExceptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemoRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val memoDao: MemoDao,
    private val conflictExceptionMessage: String
) : MemoRepository {

    override suspend fun createNewMemo(memo: Memo) = withContext(dispatcher) {
        if (!hasConflict(memo.startTime)) {
            memoDao.insert(memo)
        } else {
            throw CustomExceptions.ConflictException(conflictExceptionMessage)
        }
    }

    override suspend fun hasConflict(startTime: Long) = withContext(dispatcher) {
        memoDao.getConflicts(startTime) > 0
    }

    override suspend fun fetchUpcomingMemos(): List<Memo> = withContext(dispatcher) {
        memoDao.getUpcoming()
    }
}