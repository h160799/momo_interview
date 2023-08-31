package com.example.momo_interview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_interview.data.network.LoadApiStatus
import com.example.momo_interview.databinding.ActivityMainBinding
import com.example.momo_interview.util.Util.isInternetConnected
import com.example.momo_interview.util.Util.showNetworkErrorDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isTwoColumns = false
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (this.application as Application).repository

        val viewModel: MainViewModel by lazy {
            ViewModelProvider(this, MainViewModel.Factory(repository))[MainViewModel::class.java]
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView: RecyclerView = binding.recyclerView
        val screenWidth = resources.displayMetrics.widthPixels
        val gridLayoutManager = GridLayoutManager(this, 2)

        viewModel.status.observe(this) { status ->
            when (status) {
                LoadApiStatus.DONE -> {
                    viewModel.products.observe(this) { products ->
                        if (products!=null){
                            setLoader(View.GONE)
                            adapter = MainAdapter(products.promo, products.item, screenWidth, isTwoColumns) { newColumnState ->
                                isTwoColumns = newColumnState
                                (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = getSpanSizeLookup()
                                adapter.notifyDataSetChanged()
                            }

                            recyclerView.layoutManager = gridLayoutManager
                            recyclerView.adapter = adapter
                        }else{
                            setLoader(View.VISIBLE)
                        }
                    }

                    gridLayoutManager.spanSizeLookup = getSpanSizeLookup()

                    binding.goToTop.setOnClickListener {
                        binding.recyclerView.scrollToPosition(0)
                    }

                    recyclerView.layoutManager = gridLayoutManager

                    if (::adapter.isInitialized) {
                        recyclerView.adapter = adapter
                    }

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
                LoadApiStatus.ERROR -> {
                    if (!isInternetConnected()) {
                        showNetworkErrorDialog(this)
                    }
                }
                else -> {}
            }
        }
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
    private fun setLoader(isVisible: Int) {
        binding.progressBar.visibility = isVisible
    }
}