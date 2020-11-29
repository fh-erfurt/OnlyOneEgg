package de.fherfurt.onlyoneegg.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel


class DashboardViewModel(application: Application): AndroidViewModel(application) {



    val book="lala"
    val cookBooks = mutableListOf("Salads", "Desserts", "Pasta")
    fun onClick(){

    }
}