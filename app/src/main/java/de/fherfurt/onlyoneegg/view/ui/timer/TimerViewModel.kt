package de.fherfurt.onlyoneegg.view.ui.timer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import kotlinx.coroutines.launch

class TimerViewModel(application: Application): AndroidViewModel(application) {
    val time = "now";


    // TODO Remove all of this - just used for testing purposes
    val database = OOEDatabase.getInstance(application).ingredientDao

    val testIngredient = Ingredient(myRecipeId = 1)

    fun onInsertIngredient() {
        testIngredient.name = "s"
        testIngredient.value = 23
        //testIngredient.measurement = Measurement.l
        testIngredient.myRecipeId = 1
        viewModelScope.launch {
            val tonight = database.insert(testIngredient) ?: return@launch
        }
    }
}