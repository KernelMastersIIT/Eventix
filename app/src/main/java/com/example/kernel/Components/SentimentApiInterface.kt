package com.example.kernel.Components


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

import retrofit2.http.Query
interface SentimentApiInterface {
    @POST("analyze/")
    fun analyzeText(
        @Body input :InputText

    ): Call<SentimentData>
}
