package com.example.momo_interview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_interview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isTwoColumns = false
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView: RecyclerView = binding.recyclerView
        val screenWidth = resources.displayMetrics.widthPixels

        // 創建 Adapter 並將 GridLayout 作為標題加入
        val headerData = listOf("promo", "promo", "promo", "promo", "promo", "promo", "promo", "promo", "promo")
        val itemData = listOf(1,2,3,4,5,6,7,8,9).map { number -> MainAdapter.Item(number, false) }

        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.spanSizeLookup = getSpanSizeLookup()

        adapter = MainAdapter(headerData,itemData,screenWidth, isTwoColumns) { newColumnState ->
            isTwoColumns = newColumnState

            (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = getSpanSizeLookup()

            adapter.notifyDataSetChanged()
        }

        binding.goToTop.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
        }

        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter

        val stickyHeaderDecoration = StickyHeaderDecoration(object : StickyHeaderDecoration.HeaderClickListener {
            override fun onHeaderClicked() {
                isTwoColumns = !isTwoColumns
                adapter.notifyDataSetChanged()
            }
        })

        recyclerView.addItemDecoration(stickyHeaderDecoration)

        recyclerView.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_DOWN) {
                    val headerHeight = stickyHeaderDecoration.getHeaderHeight(rv)
                    if (e.y <= headerHeight) {
                        stickyHeaderDecoration.clickListener.onHeaderClicked()
                        return true
                    }
                }
                return false
            }
        })

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition()

                if (firstVisibleItem > 1) {
                    val totalItemCount = gridLayoutManager.itemCount - 2
                    val lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition()
                    val remainingItems = totalItemCount - lastVisibleItem + 1
                    binding.good.text = "剩餘 ${remainingItems} 項商品"
                    binding.good.visibility = View.VISIBLE
                    binding.goToTop.visibility = View.VISIBLE
                } else {
                    binding.good.visibility = View.GONE
                    binding.goToTop.visibility = View.GONE
                }
            }
        })
    }

    private fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    MainAdapter.HEADER_VIEW_TYPE, MainAdapter.STICKY_HEADER_VIEW_TYPE -> 2
                    else -> if (isTwoColumns) 1 else 2
                }
            }
        }
    }
}