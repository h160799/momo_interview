package com.example.momo_interview.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.momo_interview.Application

object Util {
    fun isInternetConnected(): Boolean {
        val cm = Application.instance
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

     fun showNetworkErrorDialog(activity: Activity?) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("請檢查你的網路並重新啟動。")
        builder.setPositiveButton("確定", null)
        val dialog = builder.create()
        dialog.show()
    }
}

