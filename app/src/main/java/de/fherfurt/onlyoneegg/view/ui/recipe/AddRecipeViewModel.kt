package de.fherfurt.onlyoneegg.view.ui.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.storage.OOEDatabase

class AddRecipeViewModel (application: Application): AndroidViewModel(application) {
    val recipeDao = OOEDatabase.getInstance(application).recipeDao;
    var ingredientList = ArrayList<Ingredient>();
}