package com.publicprojects.memo.model.repository

import com.publicprojects.memo.model.Memo

interface MemoRepository {
    suspend fun createNewMemo(memo: Memo)
    suspend fun hasConflict(startTime: Long): Boolean
    suspend fun fetchUpcomingMemos(): List<Memo>
}