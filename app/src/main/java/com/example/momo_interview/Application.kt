package com.example.momo_interview

import android.app.Application
import com.example.momo_interview.data.Repository
import com.example.momo_interview.util.ServiceLocator
import kotlin.properties.Delegates

class Application : Application() {

    val repository: Repository
        get() = ServiceLocator.provideTasksRepository(this)


    companion object {
        var instance: Application by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
