package com.example.momo_interview.data.network

import com.example.momo_interview.data.ProductListResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val HOST_NAME = "api.appworks-school.tw"
private const val API_VERSION = "1.0"
private const val BASE_URL = "https://$HOST_NAME/api/$API_VERSION/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {

    @GET("products/all")
    suspend fun getProductList(): ProductListResult
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MomoApi {
    val retrofitService : ApiService by lazy { retrofit.create(ApiService::class.java) }
}