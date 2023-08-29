package com.example.momo_interview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_interview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView: RecyclerView = binding.recyclerView
        val screenWidth = resources.displayMetrics.widthPixels

        // 設置 RecyclerView 的 layoutManager
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // 創建 Adapter 並將 GridLayout 作為標題加入
        val headerData = listOf("promo", "promo", "promo", "promo", "promo", "promo", "promo", "promo", "promo")
        val itemData = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
        val adapter = MainAdapter(headerData,itemData,screenWidth)
        recyclerView.adapter = adapter
    }
}