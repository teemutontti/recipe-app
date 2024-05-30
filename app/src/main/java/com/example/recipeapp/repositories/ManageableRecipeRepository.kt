package com.example.recipeapp.repositories

interface ManageableRecipeRepository<T> {
    suspend fun getAll(): List<T>
    suspend fun getById(id: Int): T?
    suspend fun add(r: T)
    suspend fun delete(r: T)
}

/*
object RecipeRepository: ViewModel() {
    private val TODAYS_SPECIALS = listOf("breakfast", "lunch", "dinner", "snack")

    private var _specials: SnapshotStateList<CachedRecipe> = mutableStateListOf()
    val specials: List<CachedRecipe> get() = _specials

    private var _searchResults: SnapshotStateList<CachedRecipe> = mutableStateListOf()
    val searchResults: List<CachedRecipe> get() = _searchResults

    private var _favourites: SnapshotStateList<CachedRecipe> = mutableStateListOf()
    val favourites: List<CachedRecipe> get() = _favourites

    private var _selectedRecipe: Recipe? = null
    val selectedRecipe: Recipe? get() = _selectedRecipe

    private var _ownRecipes: SnapshotStateList<Recipe> = mutableStateListOf()
    val ownRecipes: List<Recipe> get() = _ownRecipes

    private var _recipeInAddition: Recipe? = null
    val recipeInAddition: Recipe? get() = _recipeInAddition

    private suspend fun fetchRandomMeal(context: Context, mealType: String): Recipe? {
        try {
            val response = RetrofitInstance().recipeService.getRandomRecipes(
                apiKey = API_KEY,
                includeTags = mealType,
                number = 1
            )

            if (response.isSuccessful) {
                val recipe: Recipe? = response.body()?.recipes?.get(0)
                if (recipe != null) return recipe
            }
        } catch (e: Exception) {
            Log.d("ERROR", "Error in fetchRandomMeal: ${e.printStackTrace()}")
        }
        return null
    }

    suspend fun fetchNewTodaysSpecials(context: Context): List<CachedRecipe>? {
        val newSpecials: List<CachedRecipe> = TODAYS_SPECIALS.map {
            // If even one recipe fetch result is null return null
            val recipe: Recipe = fetchRandomMeal(context, it) ?: return null

            // Return a CachedRecipe object for the map function
            CachedRecipe(recipe.id, recipe.image, recipe.title)
        }
        SharedPreferencesManager.saveTodaysSpecials(context, newSpecials)
        return newSpecials
    }

    suspend fun fetchTodaysSpecials(context: Context) {
        try {
            _specials.clear()

            // Check if specials are already loaded for today
            val isSpecialsLoaded = SharedPreferencesManager.isTodaysSpecialsLoaded(context)

            var newSpecials: List<CachedRecipe>?
            if (isSpecialsLoaded) {
                // Trying to get specials from SharedPrefs
                newSpecials = SharedPreferencesManager.getTodaysSpecials(context)

                // If specials are not found in SharedPrefs, fetch new ones from API
                // This can happen if old version of the app is updated to more recent one
                // and user has some SharedPrefs saved
                if (newSpecials == null) {
                    newSpecials = fetchNewTodaysSpecials(context)
                }
            } else {
                // Fetch new specials form API
                newSpecials = fetchNewTodaysSpecials(context)
            }

            if (newSpecials != null) {
                _specials.addAll(newSpecials)
            }
        } catch (e: Exception) {
            Log.d("ERROR", "Error in fetchRandomRecipes: ${e.printStackTrace()}")
        }
    }

    suspend fun fetchRecipeById(context: Context, id: Int) {
        try {
            val response: Response<Recipe> = RetrofitInstance().recipeService.getRecipeInformation(
                apiKey = API_KEY,
                id = id
            )
            if (response.isSuccessful) {
                _selectedRecipe = response.body()
            } else {
                Log.d("API", "Error in fetching: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.d("ERROR", "Error in fetchRecipeById: ${e.printStackTrace()}")
        }
    }

    fun setSelectedRecipe(recipe: Recipe) {
        _selectedRecipe = recipe
    }

    suspend fun searchRecipes(context: Context, query: String) {
        try {
            _searchResults.clear()
            val response: Response<SearchResponse> = RetrofitInstance().recipeService.searchRecipes(
                apiKey = API_KEY,
                query = query,
                number = 10
            )
            if (response.isSuccessful) {
                response.body()?.results?.map {
                    _searchResults.add(
                        CachedRecipe(
                            id = it.id,
                            image = it.image,
                            title = it.title
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.d("ERROR", "Error in searchRecipes: ${e.printStackTrace()}")
        }
    }

    fun fetchFavourites(context: Context) {
        try {
            val favourites = SharedPreferencesManager.getFavourites(context)
            if (favourites.isNotEmpty()) {
                for (recipe in favourites) {
                    _favourites.add(recipe)
                }
            }
        } catch (e: Exception) {
            Log.d("ERROR", "Error in fetchFavourites: ${e.printStackTrace()}")
        }
    }

    fun addFavourite(context: Context, recipe: Recipe) {
        try {
            _favourites.add(
                CachedRecipe(
                    id = recipe.id,
                    image = recipe.image,
                    title = recipe.title
                )
            )
            SharedPreferencesManager.setFavourites(context, _favourites)
        } catch (e: Exception) {
            Log.d("ERROR", "Error in addFavourite: ${e.printStackTrace()}")
        }
    }

    fun deleteFavourite(context: Context, recipe: Recipe) {
        try {
            val newFavourites = _favourites.filter { it.id != recipe.id }
            _favourites.clear()
            newFavourites.forEach {
                _favourites.add(it)
            }
            SharedPreferencesManager.setFavourites(context, _favourites)
        } catch (e: Exception) {
            Log.d("ERROR", "Error in deleteFavourite: ${e.printStackTrace()}")
        }
    }

    fun fetchOwnRecipes(context: Context) {
        try {
            val newOwnRecipes = SharedPreferencesManager.getOwnRecipes(context)
            _ownRecipes.addAll(newOwnRecipes)
        } catch (e: Exception) {
            Log.d("ERROR", "Error in fetchOwnRecipes: ${e.printStackTrace()}")
        }
    }

    fun addOwnRecipe(context: Context, recipe: Recipe) {
        try {
            _ownRecipes.add(recipe)
            SharedPreferencesManager.setOwnRecipes(context, _ownRecipes)
        } catch (e: Exception) {
            Log.d("ERROR", "Error in addOwnRecipe: ${e.printStackTrace()}")
        }
    }

    fun deleteOwnRecipe(context: Context) {
        try {
            val indexOfDeletable = _ownRecipes.indexOf(_selectedRecipe)
            _ownRecipes.removeAt(indexOfDeletable)
            SharedPreferencesManager.setOwnRecipes(context, _ownRecipes)
        } catch (e: Exception) {
            Log.d("ERROR", "Error in deleteOwnRecipe: ${e.printStackTrace()}")
        }
    }

    fun updateOwnRecipe(context: Context, recipeId: Int) {
        try {
            Log.d("SharedPrefs", "index of updatable: $recipeId")

            // On not found returns -1
            val indexOfUpdatable = _ownRecipes.indexOf(
                _ownRecipes.find { it.id.toInt() == recipeId }
            )

            _recipeInAddition?.let {
                if (indexOfUpdatable != -1) {
                    _ownRecipes[indexOfUpdatable] = it
                    SharedPreferencesManager.setOwnRecipes(context, _ownRecipes)
                }
            }
        } catch (e: Exception) {
            Log.d("ERROR", "Error in updateOwnRecipe: ${e.printStackTrace()}")
        }
    }

    fun getOwnRecipesMaxId(context: Context): Int? {
        try {
            val ownRecipes = SharedPreferencesManager.getOwnRecipes(context)
            val ids = ownRecipes.map { it.id.toInt() }
            return ids.maxOrNull()
        } catch (e: Exception) {
            Log.d("ERROR", "Error in getOwnRecipesMaxId: ${e.printStackTrace()}")
        }
        return null
    }

    fun setRecipeInAddition(newRecipe: Recipe?) {
        _recipeInAddition = newRecipe
    }
}
*/