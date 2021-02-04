package de.fherfurt.onlyoneegg.model

import androidx.room.Embedded
import androidx.room.Relation

/*
* Represents relation 1:N between a recipe and ingredients
* */
data class RecipeWithIngredients(
        @Embedded val recipe: Recipe,
        @Relation(
                parentColumn = "id",
                entityColumn = "recipeId"
        )
        val ingredients: List<Ingredient>
)
