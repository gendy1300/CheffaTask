package com.agendy.chefaa.utils.retrofit

import android.content.Context
import com.agendy.chefaa.R
import com.agendy.chefaa.utils.retrofit.interceptor.AuthInterceptor
import com.google.gson.GsonBuilder
import com.agendy.chefaa.utils.retrofit.interceptor.NetworkInterceptor
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RetrofitClient @Inject constructor(
    @ApplicationContext val context: Context
) {
    fun <T> buildApi(api: Class<T>,baseUrl:String): T {

        val gson = GsonBuilder()
            .setLenient()
            .create()


        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)



        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClient.Builder()
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(NetworkInterceptor(context))
                    .addInterceptor(AuthInterceptor(context.getString(R.string.marvel_api_key)))
                    .also {
                        it.addInterceptor(logging)
                    }
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(api)


    }

}