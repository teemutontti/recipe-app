package com.example.recipeapp.models

import com.example.recipeapp.models.room.FavouriteRecipe
import com.example.recipeapp.utils.Utils.emptyRecipe

/**
 * Data class representing a Spoonacular recipe.
 *
 * @property id The unique identifier of the recipe.
 * @property title The title of the recipe.
 * @property image The URL of the recipe image.
 * @property servings The number of servings the recipe yields.
 * @property readyInMinutes The time required to prepare the recipe.
 * @property license The license under which the recipe is provided.
 * @property sourceName The name of the recipe source.
 * @property sourceUrl The URL of the recipe source.
 * @property extendedIngredients The list of extended ingredients used in the recipe.
 * @property analyzedInstructions The list of analyzed instructions for preparing the recipe.
 */
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
    /**
     * Converts the Spoonacular recipe to a Recipe object.
     *
     * @return The converted Recipe object.
     */
    fun toRecipe(): Recipe {
        if (this.extendedIngredients != null && this.analyzedInstructions != null) {
            return emptyRecipe.copy(
                id = this.id,
                title = this.title,
                image = this.image,
                servings = this.servings,
                ingredients = this.extendedIngredients.toIngredientList(),
                instructions = this.analyzedInstructions.toInstructionList(),
            )
        } else {
            return emptyRecipe.copy(
                id = this.id,
                title = this.title,
                image = this.image,
                servings = this.servings,
            )
        }

    }
    /**
     * Converts the Spoonacular recipe to a FavouriteRecipe object.
     *
     * @return The converted FavouriteRecipe object.
     */
    fun toSavable(): FavouriteRecipe {
        return FavouriteRecipe(
            id = this.id,
            title = this.title,
            image = this.image,
        )
    }
}

/**
 * Extension function to convert a list of Spoonacular ingredients to a list of Ingredients.
 *
 * @return The list of Ingredients.
 */
private fun List<SpoonacularIngredient>.toIngredientList(): List<Ingredient> {
    return this.map {
        Ingredient(
            name = it.name,
            unit = it.measures.metric.unitShort,
            amount = it.measures.metric.amount.toFloat()
        )
    }
}

/**
 * Extension function to convert a list of Spoonacular instructions to a list of Instructions.
 *
 * @return The list of Instructions.
 */
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

/**
 * Data class representing a Spoonacular ingredient.
 *
 * @property measures The measures for the ingredient.
 * @property name The name of the ingredient.
 */
data class SpoonacularIngredient(
    val measures: Measures,
    var name: String
)

/**
 * Data class representing a Spoonacular instruction.
 *
 * @property name The name of the instruction.
 * @property steps The list of steps for the instruction.
 */
data class SpoonacularInstruction(
    val name: String,
    val steps: List<Step>
)

/**
 * Data class representing measures for a Spoonacular ingredient.
 *
 * @property metric The metric measure for the ingredient.
 * @property us The US measure for the ingredient.
 */
data class Measures(
    val metric: SingleMeasure,
    val us: SingleMeasure,
)

/**
 * Data class representing a single measure for a Spoonacular ingredient.
 *
 * @property amount The amount of the measure.
 * @property unitShort The short unit of the measure.
 */
data class SingleMeasure(
    var amount: Number,
    var unitShort: String
)

/**
 * Data class representing a step in a Spoonacular instruction.
 *
 * @property number The step number.
 * @property step The description of the step.
 */
data class Step(
    val number: Number,
    var step: String,
)

/**
 * Data class representing a search response from Spoonacular API.
 *
 * @property results The list of recipes returned in the search response.
 */
data class SearchResponse(
    val results: List<SpoonacularRecipe>
)

/**
 * Data class representing a general response from Spoonacular API.
 *
 * @property recipes The list of recipes returned in the response.
 */
data class SpoonacularResponse(
    val recipes: List<SpoonacularRecipe>
)
