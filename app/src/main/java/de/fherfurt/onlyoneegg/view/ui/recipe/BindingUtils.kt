package de.fherfurt.onlyoneegg.view.ui.recipe

import android.widget.TextView
import androidx.databinding.BindingAdapter
import de.fherfurt.onlyoneegg.model.Ingredient

@BindingAdapter("ingredientName")
fun TextView.setIngredientName (item: Ingredient) {
    text = item.name
}

@BindingAdapter("ingredientAmount")
fun TextView.setIngredientAmount (item: Ingredient) {
    text = item.name
}

@BindingAdapter("ingredientMeasurement")
fun TextView.setIngredientMeasurement (item: Ingredient) {
    text = item.name
}
