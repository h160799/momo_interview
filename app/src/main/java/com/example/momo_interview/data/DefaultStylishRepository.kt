package com.example.momo_interview.data


class DefaultRepository(private val remoteDataSource: DataSource,
) : Repository {

    override suspend fun getProductList(): ApiResult<ProductListResult> {
        return remoteDataSource.getProductList()
    }


}
