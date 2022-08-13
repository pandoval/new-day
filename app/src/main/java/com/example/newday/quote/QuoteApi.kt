package com.example.newday.quote

import retrofit2.http.GET

interface QuoteApi {

    companion object {
        const val BASE_URL = "https://zenquotes.io/api/"
    }

    @GET("today")
    suspend fun getDailyQuote(): Quote
}