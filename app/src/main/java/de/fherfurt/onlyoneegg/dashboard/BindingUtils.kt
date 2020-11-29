package de.fherfurt.onlyoneegg.dashboard

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import de.fherfurt.onlyoneegg.R


@BindingAdapter("cookbookImage")
fun ImageView.setSleepImage(item: Cookbook) {
    setImageResource(R.drawable.salad)
}

@BindingAdapter("cookbookName")
fun TextView.setSleepQualityString(item: Cookbook) {
    text = item.cookbookName
}
