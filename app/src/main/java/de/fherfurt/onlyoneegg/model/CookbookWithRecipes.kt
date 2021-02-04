package de.fherfurt.onlyoneegg.model

import androidx.room.Embedded
import androidx.room.Relation

/*
* Represents relation 1:N between Cookbook and Recipe entity classes
*
* */
data class CookbookWithRecipes(
        @Embedded val cookbook: Cookbook,
        @Relation(
                parentColumn = "id",
                entityColumn = "cookbookId"
        )
        val recipeList: List<Recipe>
)

