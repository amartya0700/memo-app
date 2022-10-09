package com.publicprojects.memo.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.publicprojects.memo.R
import com.publicprojects.memo.databinding.FragmentShowUpcomingMemoBinding
import com.publicprojects.memo.util.Directions
import com.publicprojects.memo.util.clickWithDebounce
import com.publicprojects.memo.util.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowUpcomingMemoFragment : Fragment(R.layout.fragment_show_upcoming_memo) {

    private lateinit var binding: FragmentShowUpcomingMemoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentShowUpcomingMemoBinding.bind(view)

        binding.fabAddNewMemo.clickWithDebounce {
            requireView().navigate(Directions.showUpcomingMemo_createMemo)
        }
    }
}