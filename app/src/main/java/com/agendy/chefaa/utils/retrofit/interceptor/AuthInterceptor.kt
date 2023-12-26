package com.agendy.chefaa.utils.retrofit.interceptor

import com.agendy.chefaa.utils.generateMarvelHash
import com.agendy.chefaa.utils.getCurrentHourUsingLocalTime
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val token: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {


        val request = chain.request().url.newBuilder()
            .addQueryParameter("apikey", token)
            .addQueryParameter("ts",getCurrentHourUsingLocalTime())
            .addQueryParameter("hash",generateMarvelHash())
            .build()



        val builder: Request.Builder = chain.request().newBuilder().url(request)

        builder.addHeader("Accept", "application/json")

        builder.build()


        return chain.proceed(builder.build())
    }
}