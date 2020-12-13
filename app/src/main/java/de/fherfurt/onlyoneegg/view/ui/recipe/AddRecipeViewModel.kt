package de.fherfurt.onlyoneegg.view.ui.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.fherfurt.onlyoneegg.model.Ingredient

class AddRecipeViewModel (application: Application): AndroidViewModel(application) {

    var ingredientList = ArrayList<Ingredient>()
}