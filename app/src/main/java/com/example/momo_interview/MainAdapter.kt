package com.example.momo_interview

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_interview.data.Item
import com.example.momo_interview.data.Promo
import com.example.momo_interview.databinding.HeaderGridLayoutBinding
import com.example.momo_interview.databinding.ItemLayoutBinding
import com.example.momo_interview.databinding.StickyHeaderLayoutBinding

class MainAdapter (private val headerData: List<Promo>, private val itemData: List<Item>, private val screenWidth: Int, private var isTwoColumns: Boolean, private val onToggle: (Boolean) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val HEADER_VIEW_TYPE = 0
        const val STICKY_HEADER_VIEW_TYPE = 1
        const val ITEM_VIEW_TYPE = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HEADER_VIEW_TYPE -> {
                val binding = HeaderGridLayoutBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding.root)
            }
            STICKY_HEADER_VIEW_TYPE-> {
                val binding = StickyHeaderLayoutBinding.inflate(inflater, parent, false)
                StickyHeaderViewHolder(binding.root)
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
                if (position == 0) {
                    holder.bind(headerData, screenWidth)
                }
            }
            is StickyHeaderViewHolder -> {
                holder.bind()
                holder.itemView.setOnClickListener {
                        isTwoColumns = !isTwoColumns
                        onToggle(isTwoColumns)
                }
            }
            is ItemViewHolder -> {
                // 處理 item 的內容
                holder.bind(itemData[position - 2])
            }
        }
    }

    override fun getItemCount(): Int = itemData.size + 2  // 考慮加上 header

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEADER_VIEW_TYPE
            1 -> STICKY_HEADER_VIEW_TYPE
            else -> ITEM_VIEW_TYPE
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = HeaderGridLayoutBinding.bind(itemView)

        fun bind(headerData: List<Promo>, screenWidth: Int) {
            val gridLayout = binding.gridLayout
            gridLayout.removeAllViews()

            val columnCount = 3
            val cellMarginInDp = 8 // 原来的像素值
            val scale: Float = gridLayout.context.resources.displayMetrics.density
            val cellMarginInPx = (cellMarginInDp * scale + 0.5f).toInt()
            val screenWidthWithoutMargins = screenWidth - (columnCount + 1) * cellMarginInPx
            val cellSize = screenWidthWithoutMargins / columnCount

            for (i in 0 until columnCount) {
                for (j in 0 until columnCount) {
                    val cellLayout = FrameLayout(gridLayout.context)
                    val textView = TextView(cellLayout.context)
                    textView.text = headerData[i * columnCount + j].title

                    val cellLayoutParams = GridLayout.LayoutParams()
                    cellLayoutParams.width = 0
                    cellLayoutParams.height = cellSize
                    cellLayoutParams.columnSpec = GridLayout.spec(j, 1f)
                    cellLayoutParams.rowSpec = GridLayout.spec(i)
                    cellLayoutParams.setMargins(cellMarginInPx, cellMarginInPx, cellMarginInPx, cellMarginInPx)
                    cellLayout.layoutParams = cellLayoutParams
                    cellLayout.setBackgroundResource(R.drawable.layout_background)

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

    class StickyHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = StickyHeaderLayoutBinding.bind(itemView)

        fun bind() {
                binding.text.text = "filter"
            }
        }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemLayoutBinding.bind(itemView)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: Item) {
            binding.text.text = item.number

            binding.favorite.setImageDrawable(
                binding.favorite.context.getDrawable(
                    if (item.isFavorite) R.drawable.icon_favorite_selected else R.drawable.icon_favorite
                )
            )

            binding.favorite.setOnClickListener {
                item.isFavorite = !item.isFavorite
                binding.favorite.setImageDrawable(
                    binding.favorite.context.getDrawable(
                        if (item.isFavorite) R.drawable.icon_favorite_selected else R.drawable.icon_favorite
                    )
                )
            }
        }
    }
}