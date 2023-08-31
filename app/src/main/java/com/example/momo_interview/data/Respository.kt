package com.example.momo_interview.data


interface Repository {
    suspend fun getProductList(): ApiResult<ProductListResult>
}
