package com.publicprojects.memo.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.publicprojects.memo.R
import com.publicprojects.memo.databinding.FragmentCreateMemoBinding
import com.publicprojects.memo.util.*
import com.publicprojects.memo.viewmodel.MemoViewModel

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

    private val viewModel by activityViewModels<MemoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCreateMemoBinding.bind(view)

        requireActivity().setToolbarBackButton(binding.toolbar)

        binding.btnSaveMemo.clickWithDebounce {
            //
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
            binding.tilDate.editText?.setText(Utils.getDateFromTS(it))
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
    }
}