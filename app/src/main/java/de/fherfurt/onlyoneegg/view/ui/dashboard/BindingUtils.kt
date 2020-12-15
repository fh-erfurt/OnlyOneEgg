package de.fherfurt.onlyoneegg.view.ui.dashboard

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Recipe


@BindingAdapter("cookbookImage")
fun ImageView.setCookbookImage(item: Cookbook) {
    setImageResource(R.drawable.salad)
}

@BindingAdapter("cookbookName")
fun TextView.setCookbookName(item: Cookbook) {
    text = item.cookbookName
}


@BindingAdapter("ingredientName")
fun TextView.setIngredientNameString(item: Ingredient) {
    text = item.name
}

@BindingAdapter("ingredientMeasurement")
fun TextView.setIngredientMeasurementString(item: Ingredient) {
    text = item.measurement.toString()
}

@BindingAdapter("ingredientAmount")
fun TextView.setIngredientAmountString(item: Ingredient) {
    text = item.value.toString()
}

@BindingAdapter("recipeName")
fun TextView.setRecipeName(item: Recipe) {
    text = item.name
}

@BindingAdapter("recipeImage")
fun ImageView.setRecipeImage(item: Recipe) {
    setImageResource(R.drawable.salad)
}