package com.example.recipeapp.viewmodels

data class ViewModelWrapper (
    val favourite: FavouriteRecipesViewModel,
    val personal: PersonalRecipesViewModel,
    val inspection: RecipeUnderInspectionViewModel,
    val search: SearchViewModel,
    val specials: TodaysSpecialsViewModel,
)
