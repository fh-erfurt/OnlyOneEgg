package de.fherfurt.onlyoneegg.view.ui.dashboard

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import de.fherfurt.onlyoneegg.R
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.model.Recipe


@BindingAdapter("cookbookImage")
fun ImageView.setSleepImage(item: Cookbook) {
    setImageResource(R.drawable.salad)
}

@BindingAdapter("cookbookName")
fun TextView.setSleepQualityString(item: Cookbook) {
    text = item.cookbookName
}
