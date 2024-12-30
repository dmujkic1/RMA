package com.example.spirala1

import com.google.gson.annotations.SerializedName

data class PlantDetailsResponse(
    @SerializedName("data") var data: PlantDetail
)

data class PlantDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("common_name") val common_name: String?,
    @SerializedName("scientific_name") val scientific_name: String,
    @SerializedName("family_common_name") val family_common_name: String?,
    @SerializedName("main_species") val main_species: PlantSpecies
)

data class PlantSpecies(
    @SerializedName("family")  val family: String,
    @SerializedName("image_url")  val image_url: String?,
    @SerializedName("specifications") val specifications: PlantSpecification,
    @SerializedName("growth") val growth: PlantGrowth,
    @SerializedName("flower") val flower: FlowerColor,
    @SerializedName("edible") val edible: Boolean?
)



data class FlowerColor(
    @SerializedName("color") val color: List<String>?
)

data class PlantGrowth(
    @SerializedName("light") val light: Int?,
    @SerializedName("atmospheric_humidity") val atmospheric_humidity: Int?,
    @SerializedName("soil_texture") val soil_texture: Int?
    )

data class PlantSpecification(
    @SerializedName("toxicity") val toxicity: String?
)

