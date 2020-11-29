package de.fherfurt.onlyoneegg.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


class DashboardViewModel(application: Application): AndroidViewModel(application) {



    val book="lala"
    val cookbooks : List<String>
        get() = mutableListOf("Salads", "Desserts", "Pasta")


    fun onClick(){

    }
}