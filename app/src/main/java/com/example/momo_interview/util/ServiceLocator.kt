package com.example.momo_interview.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.momo_interview.data.DataSource
import com.example.momo_interview.data.DefaultRepository
import com.example.momo_interview.data.Repository
import com.example.momo_interview.data.network.RemoteDataSource

object ServiceLocator {

    @Volatile
    var repository: Repository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): Repository {
        synchronized(this) {
            return repository
                ?: repository
                ?: createRepository(context)
        }
    }

    private fun createRepository(context: Context): Repository {
        return DefaultRepository(
            RemoteDataSource,
        )
    }

}