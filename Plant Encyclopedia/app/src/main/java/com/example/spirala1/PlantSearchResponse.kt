package com.example.spirala1

import com.google.gson.annotations.SerializedName

data class PlantSearchResponse(
    @SerializedName("data") var data: List<PlantResponse>
)

data class PlantResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("common_name") val common_name: String?,
    @SerializedName("scientific_name") val scientific_name: String,
    @SerializedName("family_common_name") val family_common_name: String?,
    @SerializedName("family")  val family: String,
    @SerializedName("image_url")  val image_url: String?
)
