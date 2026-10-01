package com.example.dictionary.api

import com.example.dictionary.model.DictionaryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("entries/en/{word}")
    suspend fun getWord(
        @Path("word") word: String
    ): List<DictionaryResponse>
}