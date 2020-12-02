package de.fherfurt.onlyoneegg.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class RecipeViewModel (application: Application): AndroidViewModel(application) {

    val recipeName ="Greek Chicken Skewers"

    val recipe ="Whisk lemon juice, oil, vinegar, onion flakes, garlic, lemon zest, Greek seasoning," +
            " poultry seasoning, oregano, pepper, and thyme together in a bowl and pour into a resealable plastic bag."

}