package com.example.recipeapp.models

import com.example.recipeapp.models.room.PersonalIngredient
import com.example.recipeapp.models.room.PersonalInstruction
import com.example.recipeapp.models.room.PersonalRecipe

data class SpoonacularRecipe(
    override val id: Number,
    override val title: String,
    override val image: String,
    override val servings: Number,
    val readyInMinutes: Number,
    val license: String,
    val sourceName: String,
    val sourceUrl: String,
    val extendedIngredients: List<SpoonacularIngredient>,
    val analyzedInstructions: List<SpoonacularInstruction>
): Recipe {
    fun toCachedRecipe(): CachedRecipe {
        return CachedRecipe(
            id = this.id.toInt(),
            image = this.image,
            title = this.title
        )
    }

    fun toPersonalRecipe(): PersonalRecipe {
        val ingredients = this.extendedIngredients.map {
            PersonalIngredient(
                name = it.name,
                amount = it.measures.metric.amount.toFloat(),
                unit = it.measures.metric.unitShort
            )
        }
        val instructions = this.analyzedInstructions[0].steps.map {
            PersonalInstruction(
                number = it.number.toInt(),
                step = it.step
            )
        }
        return PersonalRecipe(
            id = this.id.toInt(),
            title = this.title,
            image = this.image,
            servings = this.servings.toInt(),
            ingredients = ingredients,
            instructions = instructions
        )
    }
}

data class SpoonacularIngredient(
    val measures: Measures,
    var name: String
)

data class SpoonacularInstruction(
    val name: String,
    val steps: List<Step>
)

data class Measures(
    val metric: SingleMeasure,
    val us: SingleMeasure,
)

data class SingleMeasure(
    var amount: Number,
    var unitShort: String
)

data class Step(
    val number: Number,
    var step: String,
)

data class SearchResponse(
    val results: List<CachedRecipe>
)
data class SpoonacularResponse(
    val recipes: List<SpoonacularRecipe>
)
