package de.fherfurt.onlyoneegg.model

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeWithIngredients(
        @Embedded val recipe:Recipe,
        @Relation(
                parentColumn = "recipeId",
                entityColumn = "myRecipeId"
        )
        val ingredients:List<Ingredient>
)
