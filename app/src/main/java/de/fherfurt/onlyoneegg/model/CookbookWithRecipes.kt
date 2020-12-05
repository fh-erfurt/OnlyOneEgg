package de.fherfurt.onlyoneegg.model

import androidx.room.Embedded
import androidx.room.Relation

data class CookbookWithRecipes(
        @Embedded val cookbook: Cookbook,
        @Relation(
                parentColumn = "cookbookId",
                entityColumn = "myCookbookId"
        )
        val recipeList:List<Recipe>
)

