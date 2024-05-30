package com.example.recipeapp.models

import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.utils.Utils.emptyRecipe

data class SpoonacularRecipe(
    val id: Int,
    val title: String,
    val image: String,
    val servings: Int,
    val readyInMinutes: Number,
    val license: String,
    val sourceName: String,
    val sourceUrl: String,
    val extendedIngredients: List<SpoonacularIngredient>,
    val analyzedInstructions: List<SpoonacularInstruction>,
){
    fun toRecipe(): Recipe {
        return emptyRecipe.copy(
            id = this.id,
            title = this.title,
            image = this.image,
            servings = this.servings,
            ingredients = this.extendedIngredients.toIngredientList(),
            instructions = this.analyzedInstructions.toInstructionList(),
        )
    }
    fun toSavable(): FavouriteRecipe {
        return FavouriteRecipe(
            id = this.id,
            title = this.title,
            image = this.image,
        )
    }
}

private fun List<SpoonacularIngredient>.toIngredientList(): List<Ingredient> {
    return this.map {
        Ingredient(
            name = it.name,
            unit = it.measures.metric.unitShort,
            amount = it.measures.metric.amount.toFloat()
        )
    }
}

private fun List<SpoonacularInstruction>.toInstructionList(): List<Instruction> {
    val newInstructions = mutableListOf<Instruction>()
    this.forEach { instruction ->
        instruction.steps.forEach { step ->
            newInstructions.add(
                Instruction(
                    number = step.number.toInt(),
                    text = step.step
                )
            )
        }
    }
    return newInstructions
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
    val results: List<SpoonacularRecipe>
)
data class SpoonacularResponse(
    val recipes: List<SpoonacularRecipe>
)
