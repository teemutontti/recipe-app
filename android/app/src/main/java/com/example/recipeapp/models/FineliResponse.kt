package com.example.recipeapp.models

data class FineliName(
    val fi: String,
    val sv: String,
    val en: String,
    val la: String,
)

data class FineliResponse(
    val id: Int,
    val name: FineliName,
    val salt: Double,
    val energyKcal: Double,
    val energy: Double,
    val fat: Double,
    val protein: Double,
    val carbohydrate: Double,
    val alcohol: Double,
    val organicAcids: Double,
    val sugarAlcohol: Double,
    val saturatedFat: Double,
    val fiber: Double,
    val sugar: Double
) {
    fun toFood(): Food {
        return Food(
            name = this.name.en,
            barcode = "",
            servingSize = 100,
            calories = this.energyKcal,
            carbs = this.carbohydrate,
            createdBy = 514371,
            editedBy = 514371,
            fat = this.fat,
            protein = this.protein

        )
    }
}