package com.example.momo_interview.data


interface DataSource {
    suspend fun getProductList(): ApiResult<ProductListResult>
}
