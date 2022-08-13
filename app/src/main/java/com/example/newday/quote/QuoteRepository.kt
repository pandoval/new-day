package com.example.newday.quote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class QuoteRepository(private val scope: CoroutineScope) {

    fun getDailyQuote(): MutableLiveData<Quote> {

        val quote = MutableLiveData<Quote>()

        scope.launch(Dispatchers.IO) {
            val retrofit = Retrofit.Builder().baseUrl(QuoteApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val quoteApi = retrofit.create(QuoteApi::class.java)
            quote.postValue(quoteApi.getDailyQuote())
        }

        return quote
    }
}