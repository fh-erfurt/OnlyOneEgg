package de.fherfurt.onlyoneegg.model


//Data class to combine the important attributes from Recipes and Ingredients for Exporting a file
data class  ExportRecipe(

    var name: String,

    var description: String,

    var cooktime: Float,

    var difficulty: Difficulty,

    var ingredient: List<ExportIngredient>

)
{

    constructor(recipe:Recipe, ingredient: ArrayList<ExportIngredient>)
            : this(recipe.name, recipe.description, recipe.cooktime, recipe.difficulty, ingredient) {

    }
}