package com.publicprojects.memo.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.publicprojects.memo.R
import com.publicprojects.memo.databinding.FragmentCreateMemoBinding
import com.publicprojects.memo.util.*
import com.publicprojects.memo.view.sealed.UiState
import com.publicprojects.memo.viewmodel.CreateMemoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateMemoFragment : Fragment(R.layout.fragment_create_memo) {

    private val datePicker by lazy {
        MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(R.string.event_date)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build()
            )
            .build()
    }

    private val startTimePicker by lazy {
        MaterialTimePicker.Builder()
            .setTitleText(R.string.start_time)
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .build()
    }

    private val endTimePicker by lazy {
        MaterialTimePicker.Builder()
            .setTitleText(R.string.end_time)
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .build()
    }

    private lateinit var binding: FragmentCreateMemoBinding

    private lateinit var viewModel: CreateMemoViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCreateMemoBinding.bind(view)

        viewModel = ViewModelProvider(this)[CreateMemoViewModel::class.java]

        requireActivity().setToolbarBackButton(binding.toolbar)

        binding.btnSaveMemo.clickWithDebounce {
            viewModel.saveMemo()
        }

        datePicker.addOnPositiveButtonClickListener {
            datePicker.selection?.let {
                viewModel.setPickedDate(
                    it
                )
            }
        }

        startTimePicker.addOnPositiveButtonClickListener {
            viewModel.setPickedStartTime("${startTimePicker.hour}:${startTimePicker.minute}")
        }

        endTimePicker.addOnPositiveButtonClickListener {
            viewModel.setPickedEndTime("${endTimePicker.hour}:${endTimePicker.minute}")
        }

        binding.tilName.editText?.afterTextChange { viewModel.setEventName(it) }

        binding.tilDesc.editText?.afterTextChange { viewModel.setEventDesc(it) }

        binding.tilDate.editText?.clickWithDebounce {
            datePicker.show(
                requireActivity().supportFragmentManager,
                getString(R.string.event_date)
            )
        }

        binding.tilStartTime.editText?.clickWithDebounce {
            startTimePicker.show(
                requireActivity().supportFragmentManager,
                getString(R.string.start_time)
            )
        }

        binding.tilEndTime.editText?.clickWithDebounce {
            endTimePicker.show(
                requireActivity().supportFragmentManager,
                getString(R.string.end_time)
            )
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.eventDate.observe(viewLifecycleOwner) {
            binding.tilDate.editText?.setText(it)
        }
        viewModel.eventStartTime.observe(viewLifecycleOwner) {
            binding.tilStartTime.editText?.setText(Utils.getTimeInAmPm(it))
        }
        viewModel.eventEndTime.observe(viewLifecycleOwner) {
            binding.tilEndTime.editText?.setText(Utils.getTimeInAmPm(it))
        }
        viewModel.eventIsValid.observe(viewLifecycleOwner) {
            binding.btnSaveMemo.enabled(it)
        }
        viewModel.endTimeError.observe(viewLifecycleOwner) {
            binding.tilEndTime.error = it
        }
        viewModel.startTimeError.observe(viewLifecycleOwner) {
            binding.tilStartTime.error = it
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.createMemo.collectLatest {
                    when (it) {
                        is UiState.Idle -> Unit
                        is UiState.Success -> {
                            Toast.makeText(
                                requireContext().applicationContext, "Added new memo", Toast.LENGTH_LONG
                            ).show()
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                            viewModel.resetUiState()
                        }
                        is UiState.Failure -> {
                            Snackbar.make(
                                binding.root, it.t.message ?: "", Snackbar.LENGTH_LONG
                            ).show()
                            viewModel.resetUiState()
                        }
                    }
                }
            }
        }
    }
}