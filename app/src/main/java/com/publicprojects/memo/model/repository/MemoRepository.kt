package com.publicprojects.memo.model.repository

import com.publicprojects.memo.model.Memo

interface MemoRepository {
    suspend fun createNewMemo(memo: Memo)
    suspend fun hasConflict(startTime: Long): Boolean

    data class ConflictException(
        val msg: String = ""
    ): Exception(msg)
}