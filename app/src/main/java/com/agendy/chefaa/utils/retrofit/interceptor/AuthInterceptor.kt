package com.agendy.chefaa.utils.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val token: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {


        val request = chain.request().url.newBuilder()
            .addQueryParameter("apikey", token)
            .build()


        val builder: Request.Builder = chain.request().newBuilder().url(request)

        builder.addHeader("Accept", "application/json")

        builder.build()


        return chain.proceed(builder.build())
    }
}