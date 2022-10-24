package com.publicprojects.memo.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.publicprojects.memo.databinding.ItemHeaderDateBinding
import com.publicprojects.memo.databinding.ItemMemoBinding
import com.publicprojects.memo.model.Memo
import com.publicprojects.memo.util.Utils

class MemoAdapter(
    private val memos: MutableList<Memo> = mutableListOf()
) : Adapter<ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(memos: List<Memo>) {
        this.memos.apply {
            clear()
            addAll(memos)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == TYPE_HEADER) {
            return HeaderViewHolder(
                ItemHeaderDateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
        }
        return MemoViewHolder(
            ItemMemoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memo = memos[position]
        val timeFrame =
            "${Utils.getTimeFromTs(memo.startTime)} - ${Utils.getTimeFromTs(memo.endTime)}"
        when (holder) {
            is HeaderViewHolder -> {
                holder.binding.apply {
                    tvDate.text = Utils.getDateFromTs(memo.startTime)
                    itemMemo.tvName.text = memo.name
                    itemMemo.tvTime.text = timeFrame
                    itemMemo.tvDesc.text = memo.desc
                }
            }
            is MemoViewHolder -> {
                holder.binding.apply {
                    tvName.text = memo.name
                    tvTime.text = timeFrame
                    tvDesc.text = memo.desc
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return this.memos.size
    }

    override fun getItemViewType(position: Int): Int {
        val date = Utils.getDateFromTs(memos[position].startTime)
        if (position == 0) {
            return TYPE_HEADER
        }
        val prevDate = Utils.getDateFromTs(memos[position - 1].startTime)
        if (date != prevDate) {
            return TYPE_HEADER
        }
        return TYPE_ENTRY
    }

    inner class MemoViewHolder(val binding: ItemMemoBinding) : ViewHolder(binding.root)
    inner class HeaderViewHolder(val binding: ItemHeaderDateBinding) : ViewHolder(binding.root)

    companion object {
        private const val TYPE_ENTRY = 0
        private const val TYPE_HEADER = 1
    }
}