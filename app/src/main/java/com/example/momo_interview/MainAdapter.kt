package com.example.momo_interview

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_interview.databinding.HeaderGridLayoutBinding
import com.example.momo_interview.databinding.ItemLayoutBinding

class MainAdapter (private val headerData: List<String>, private val itemData: List<String>, private val screenWidth: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER_VIEW_TYPE = 0
    private val ITEM_VIEW_TYPE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HEADER_VIEW_TYPE -> {
                val binding = HeaderGridLayoutBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding.root)
            }
            else -> {
                val binding = ItemLayoutBinding.inflate(inflater, parent, false)

                ItemViewHolder(binding.root)

            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                // 處理 header 的內容
                holder.bind(headerData, screenWidth)
            }
            is ItemViewHolder -> {
                // 處理 item 的內容
                holder.bind(itemData)  // 注意減去 header 的位置
            }
        }
    }

    override fun getItemCount(): Int = itemData.size + 1  // 考慮加上 header

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) HEADER_VIEW_TYPE else ITEM_VIEW_TYPE
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = HeaderGridLayoutBinding.bind(itemView)

        fun bind(headerData: List<String>, screenWidth: Int) {
            val gridLayout = binding.gridLayout
            gridLayout.removeAllViews()

            val columnCount = 3
            val cellMargin = 8
            val screenWidthWithoutMargins = screenWidth - (columnCount + 1) * cellMargin
            val cellSize = screenWidthWithoutMargins / columnCount

            for (i in 0 until columnCount) {
                for (j in 0 until columnCount) {
                    val cellLayout = FrameLayout(gridLayout.context)
                    val textView = TextView(cellLayout.context)
                    textView.text = headerData[i * columnCount + j]

                    val cellLayoutParams = GridLayout.LayoutParams()
                    cellLayoutParams.width = 0
                    cellLayoutParams.height = cellSize
                    cellLayoutParams.columnSpec = GridLayout.spec(j, 1f)
                    cellLayoutParams.rowSpec = GridLayout.spec(i)
                    cellLayoutParams.setMargins(cellMargin, cellMargin, cellMargin, cellMargin)
                    cellLayout.layoutParams = cellLayoutParams
                    cellLayout.setBackgroundResource(R.drawable.grid_layout_text_background)

                    val params = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                    when (j) {
                        0 -> params.gravity = Gravity.START or Gravity.CENTER_VERTICAL
                        1 -> params.gravity = Gravity.CENTER
                        2 -> params.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    }

                    textView.layoutParams = params

                    cellLayout.addView(textView)
                    gridLayout.addView(cellLayout)
                }
            }
        }
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemLayoutBinding.bind(itemView)

        fun bind(itemData: List<String>) {
            for (i in itemData){
                binding.text.text = i

            }

        }
    }
}