package com.example.spirala1
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface TrefleApiService {
    @GET("plants/search")
    suspend fun searchPlants(@Query("q") query: String,
                             @Query("token") token: String="FfSlLp0DzAZlPT8UTdn2tPEBPmuLPeA6C0eLMpkAFRY"): Response<PlantSearchResponse>

    @GET("plants/{id}")
    suspend fun getPlantDetails(@Path("id") id: Int,
                                @Query("token") token: String="FfSlLp0DzAZlPT8UTdn2tPEBPmuLPeA6C0eLMpkAFRY"): Response<PlantDetailsResponse>

    @GET("plants/search")
    suspend fun getPlantsByFlowerColorAPI(@Query("q") query: String,
                                       @QueryMap filters: Map<String,String>,
                                       @Query("token") apiKey: String="FfSlLp0DzAZlPT8UTdn2tPEBPmuLPeA6C0eLMpkAFRY"): Response<PlantSearchResponse>
}