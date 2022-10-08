package com.publicprojects.memo.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.publicprojects.memo.model.Memo

@Database(entities = [Memo::class], version = 1)
abstract class AppDb: RoomDatabase() {
    abstract fun memoDao(): MemoDao
}