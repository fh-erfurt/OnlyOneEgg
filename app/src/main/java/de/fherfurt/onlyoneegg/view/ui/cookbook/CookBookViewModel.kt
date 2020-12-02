package de.fherfurt.onlyoneegg.view.ui.cookbook

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class CookBookViewModel (application: Application): AndroidViewModel(application) {

    val cookbookName ="Desserts"

    val recipes : List<String>
        get() = mutableListOf("Chocolate Cake", "Banana Muffins", "Apple Pie")


    fun onClick() {
    }
}