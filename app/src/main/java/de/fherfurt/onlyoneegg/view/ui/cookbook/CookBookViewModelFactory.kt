package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.fherfurt.onlyoneegg.storage.RecipeRepository


class CookBookViewModelFactory (
    private val application: Application,
    private val dataSource: RecipeRepository,
    private val cookbookId:Long) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CookBookViewModel::class.java)) {
            return CookBookViewModel(application, dataSource, cookbookId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
