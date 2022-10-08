package com.publicprojects.memo.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.publicprojects.memo.model.Memo

@Dao
interface MemoDao {
    @Query("select * from memos where start_time > :currentTs")
    fun getUpcoming(currentTs: Long = System.currentTimeMillis()): List<Memo>

    @Query("select * from memos")
    fun getAll(): List<Memo>

    @Insert
    fun insert(memo: Memo)
}