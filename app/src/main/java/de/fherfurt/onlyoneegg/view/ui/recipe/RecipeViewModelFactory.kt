package de.fherfurt.onlyoneegg.view.ui.recipe

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.fherfurt.onlyoneegg.storage.IngredientRepository

class RecipeViewModelFactory (
        private val application: Application,
        private val dataSource: IngredientRepository,
        private var recipeID : Long ) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {

            return RecipeViewModel(application, dataSource, recipeID ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}