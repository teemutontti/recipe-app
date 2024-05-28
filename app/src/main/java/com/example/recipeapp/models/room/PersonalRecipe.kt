package com.example.recipeapp.models.room

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipeapp.models.IngredientUnit
import com.example.recipeapp.models.Recipe

@Entity(tableName = "personal_recipes")
data class PersonalRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val image: String,
    val servings: Int,
    val ingredients: List<PersonalIngredient>,
    val instructions: List<PersonalInstruction>
) {

    fun toFavouriteRecipe(): FavouriteRecipe {
        return FavouriteRecipe(
            id = this.id,
            image = this.image.toString(),
            title = this.title
        )
    }
}

data class PersonalIngredient(
    val name: String,
    val unit: String,
    val amount: Float,
)

data class PersonalInstruction(
    val number: Int,
    val step: String,
)