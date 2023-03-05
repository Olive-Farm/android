package com.farmer.network

sealed class BaseResponse<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : BaseResponse<T>(data)
    class Error<T>(data: T? = null, message: String?) : BaseResponse<T>(data, message)
}
