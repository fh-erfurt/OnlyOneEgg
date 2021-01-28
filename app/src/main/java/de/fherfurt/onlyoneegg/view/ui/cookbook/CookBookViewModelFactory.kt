package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.RecipeRepository

/*
* class used to create cookbook view model
* */

class CookBookViewModelFactory (
    private val application: Application,
    private val recipeRepository: RecipeRepository,
    private val cookbookRepository: CookbookRepository,
    private val cookbookId:Long) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CookBookViewModel::class.java)) {
            return CookBookViewModel(application, recipeRepository, cookbookRepository, cookbookId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
