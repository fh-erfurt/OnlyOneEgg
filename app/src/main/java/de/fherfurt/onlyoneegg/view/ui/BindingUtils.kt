package de.fherfurt.onlyoneegg.view.ui.dashboard

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.model.Ingredient
import de.fherfurt.onlyoneegg.model.Recipe


@BindingAdapter("cookbookImage")
fun ImageView.setCookbookImage(item: Cookbook) {
    if (item.uri.isEmpty()) {
        setImageResource(R.drawable.egg)
    } else {
        setImageURI(Uri.parse(item.uri))
    }
}

@BindingAdapter("cookbookName")
fun TextView.setCookbookName(item: Cookbook) {
    text = item.name
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

@BindingAdapter("recipeCooktime")
fun TextView.setRecipeCooktime(item: Recipe) {
    text = item.cooktime.toString()
}

@BindingAdapter("difficulty")
fun TextView.setRecipeDifficulty(item: Recipe) {
    text = item.difficulty.toString()
}


@BindingAdapter("recipeImage")
fun ImageView.setRecipeImage(item: Recipe) {
    setImageResource(R.drawable.chicken)
}