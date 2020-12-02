package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CookBookViewModelFactory (private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CookBookViewModel::class.java)) {
            return CookBookViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}