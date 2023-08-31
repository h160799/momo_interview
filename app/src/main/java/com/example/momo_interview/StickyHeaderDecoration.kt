package com.example.momo_interview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class StickyHeaderDecoration(val clickListener: HeaderClickListener) : RecyclerView.ItemDecoration() {

    interface HeaderClickListener {
        fun onHeaderClicked()
    }

    private lateinit var headerView: View
    private val grayBlockHeightInDp = 140 // 原来的像素值

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        headerView = LayoutInflater.from(parent.context)
            .inflate(R.layout.header_grid_layout, parent, false)

        val topChild = parent.getChildAt(0)
        val topPosition = parent.getChildAdapterPosition(topChild ?: return)

        if (topPosition <= 1) {
            // Hide sticky header at the top
            return
        }

        drawHeader(c, headerView)
    }
    fun getHeaderHeight(rv: RecyclerView): Float {
        if (!::headerView.isInitialized) {
            headerView = LayoutInflater.from(rv.context)
                .inflate(R.layout.header_grid_layout, rv, false)
        }
        return changeDpToPx(headerView, grayBlockHeightInDp)
    }
    private fun drawHeader(c: Canvas, header: View) {
        val grayBlockHeightInPx = changeDpToPx(header, grayBlockHeightInDp)
        val marginInPx = changeDpToPx(header, 8)

        val paint = Paint().apply {
            color = Color.GRAY
        }

        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = changeDpToPx(header, 20)
            textAlign = Paint.Align.CENTER
        }

        c.drawRect(
            marginInPx,
            marginInPx + changeDpToPx(header, 20 - 8),
            c.width.toFloat() - marginInPx,
            grayBlockHeightInPx - marginInPx - changeDpToPx(header, 28 - 8),
            paint
        )

        val text = "filter"
        val textX = c.width.toFloat() / 2
        val textY = grayBlockHeightInPx / 2
        c.drawText(text, textX, textY, textPaint)

        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }

    private fun changeDpToPx(header: View, dp: Int): Float {
        val scale: Float = header.resources.displayMetrics.density
        return dp * scale + 0.5f
    }
}