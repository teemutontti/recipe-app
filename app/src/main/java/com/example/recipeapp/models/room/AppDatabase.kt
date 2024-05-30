package com.example.recipeapp.models.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.recipeapp.models.Ingredient
import com.example.recipeapp.models.Instruction
import com.google.gson.Gson

@Database(entities = [PersonalRecipe::class, FavouriteRecipe::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun personalRecipeDao(): PersonalRecipeDao
    abstract fun favouriteRecipeDao(): FavouriteRecipeDao
}

object DatabaseProvider {
    @Volatile private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = "app_db"
            ).build()

        }
        return INSTANCE as AppDatabase
    }
}

class Converters {
    @TypeConverter
    fun fromIngredientList(value: List<Ingredient>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toIngredientList(value: String?): List<Ingredient> {
        return Gson().fromJson(value, Array<Ingredient>::class.java).toList()

    }

    @TypeConverter
    fun fromInstructionList(value: List<Instruction>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toInstructionList(value: String?): List<Instruction> {
        return Gson().fromJson(value, Array<Instruction>::class.java).toList()
    }
}
