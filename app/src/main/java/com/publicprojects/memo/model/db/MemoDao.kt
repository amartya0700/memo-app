package com.publicprojects.memo.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.publicprojects.memo.model.Memo

@Dao
interface MemoDao {
    @Query("select * from memos where start_time > :currentTs order by start_time")
    fun getUpcoming(currentTs: Long = System.currentTimeMillis()): List<Memo>

    @Query("select * from memos")
    fun getAll(): List<Memo>

    @Insert
    fun insert(memo: Memo)

    @Query("select count(*) from memos where start_time <= :startTs and end_time >= :startTs")
    fun getConflicts(startTs: Long): Int
}