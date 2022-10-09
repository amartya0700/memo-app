package com.publicprojects.memo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.publicprojects.memo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)
    }
}