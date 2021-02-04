package de.fherfurt.onlyoneegg.export

import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Measurement


// Data class to minimize the Ingredient class
data class ExportIngredient(
    var name: String,
    var measurement: Measurement,
    var value: Long,
) {
    // create second constructor
    constructor(ingredient: Ingredient)
            : this(ingredient.name, ingredient.measurement, ingredient.value) {
    }
}