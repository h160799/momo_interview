package com.example.momo_interview.data.network

import com.example.momo_interview.data.ApiResult
import com.example.momo_interview.data.DataSource
import com.example.momo_interview.data.ProductListResult
import com.example.momo_interview.util.Util.isInternetConnected

object RemoteDataSource : DataSource {

    override suspend fun getProductList(): ApiResult<ProductListResult> {

        if (!isInternetConnected()) {
            return ApiResult.Fail("您尚未連接 Internet")
        }

        return try {
            val listResult = MomoApi.retrofitService.getProductList()

            listResult.error?.let {
                return ApiResult.Fail(it)
            }
            ApiResult.Success(listResult)

        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}
