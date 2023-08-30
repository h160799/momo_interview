package com.example.momo_interview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class StickyHeaderDecoration() : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val headerView = LayoutInflater.from(parent.context)
            .inflate(R.layout.header_grid_layout, parent, false)

        val topChild = parent.getChildAt(0)
        val topPosition = parent.getChildAdapterPosition(topChild ?: return)

        if (topPosition <= 1) {
            // Hide sticky header at the top
            return
        }
//        val stickyHeaderTop = maxOf(0, (topChild.top ?: 0) - headerView.height)

        drawHeader(c, headerView)
    }

    private fun drawHeader(c: Canvas, header: View) {

        val grayBlockHeightInDp = 140 // 原来的像素值
        val grayBlockHeightInPx = changeDpToPx(header, grayBlockHeightInDp)

        val paint = Paint().apply {
            color = Color.GRAY
        }

        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = changeDpToPx(header, 20)
            textAlign = Paint.Align.CENTER
        }

        c.drawRect(
            changeDpToPx(header, 16),
            changeDpToPx(header, 20),
            c.width.toFloat() - changeDpToPx(header, 16),
            grayBlockHeightInPx - changeDpToPx(header, 28),
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
