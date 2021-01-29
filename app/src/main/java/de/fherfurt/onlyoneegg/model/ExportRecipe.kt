package de.fherfurt.onlyoneegg.model



data class  ExportRecipe (

    var name: String ,

    var description: String,

    var cooktime: Float ,

    var difficulty: Difficulty ,

    var ingredient:List<Ingredient>

    )
{

    constructor(recipe:Recipe, ingredient: List<Ingredient>)
            : this(recipe.name, recipe.description, recipe.cooktime, recipe.difficulty, ingredient) {

    }
}