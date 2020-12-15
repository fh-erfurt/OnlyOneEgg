package de.fherfurt.onlyoneegg.view.ui.inputCookbook

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.fherfurt.onlyoneegg.storage.CookbookRepository


class InputCookbookViewModelFactory(
        private val application: Application,
        private val dataSource: CookbookRepository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InputCookbookViewModel::class.java)) {
            return InputCookbookViewModel(application, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}