package de.fherfurt.onlyoneegg.model

data class ExportIngredient (
    var name: String ,
   var measurement: Measurement,
    var value : Long,
        )
{

    constructor( ingredient: Ingredient)
            : this( ingredient.name,ingredient.measurement,ingredient.value) {

    }
}