package com.publicprojects.memo.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.publicprojects.memo.R
import com.publicprojects.memo.databinding.FragmentShowUpcomingMemoBinding
import com.publicprojects.memo.model.Memo
import com.publicprojects.memo.util.*
import com.publicprojects.memo.view.adapter.MemoAdapter
import com.publicprojects.memo.view.sealed.UiState
import com.publicprojects.memo.viewmodel.ShowMemoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowUpcomingMemoFragment : Fragment(R.layout.fragment_show_upcoming_memo) {

    private lateinit var binding: FragmentShowUpcomingMemoBinding
    private lateinit var showMemoViewModel: ShowMemoViewModel
    private val memoAdapter by lazy { MemoAdapter() }

    override fun onStart() {
        super.onStart()
        showMemoViewModel.getLatestMemos()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentShowUpcomingMemoBinding.bind(view)

        showMemoViewModel = ViewModelProvider(this)[ShowMemoViewModel::class.java]

        binding.fabAddNewMemo.clickWithDebounce {
            requireView().navigate(Directions.showUpcomingMemo_createMemo)
        }

        binding.rvMemos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = memoAdapter
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                showMemoViewModel.showMemo.collect {
                    when (it) {
                        is UiState.Idle -> Unit
                        is UiState.Success -> {
                            val memos = (it.res as? List<*>)?.filterIsInstance(Memo::class.java)
                                ?: emptyList()
                            if (memos.isNotEmpty()) {
                                binding.tvNoData.hide()
                                binding.rvMemos.show()
                                memoAdapter.setItems(memos)
                            } else {
                                binding.tvNoData.show()
                                binding.rvMemos.hide()
                            }
                            showMemoViewModel.resetUiState()
                        }
                        is UiState.Failure -> {
                            binding.tvNoData.show()
                            binding.rvMemos.hide()
                            Snackbar.make(
                                binding.root, it.t.message ?: "", Snackbar.LENGTH_LONG
                            ).show()
                            showMemoViewModel.resetUiState()
                        }
                    }
                }
            }
        }
    }
}