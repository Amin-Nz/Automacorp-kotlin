package com.automacorp.service

import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.Credentials
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiServices {
    private const val API_USERNAME = "user"
    private const val API_PASSWORD = "password"

    val roomsApiService: RoomsApiService by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor(API_USERNAME, API_PASSWORD))
            .build()

        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create()) // Ensure this line is present
            .client(client)
            .baseUrl("https://automacorp.devmind.cleverapps.io/api/")
            .build()
            .create(RoomsApiService::class.java)
    }

    class BasicAuthInterceptor(private val username: String, private val password: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .header("Authorization", Credentials.basic(username, password))
                .build()
            return chain.proceed(request)
        }
    }
}