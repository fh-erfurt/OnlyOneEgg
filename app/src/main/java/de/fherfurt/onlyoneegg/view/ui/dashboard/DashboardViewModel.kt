package de.fherfurt.onlyoneegg.view.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel


class DashboardViewModel(application: Application): AndroidViewModel(application) {



    val book="lala"
    val cookbooks : List<String>
        get() = mutableListOf("Salads", "Desserts", "Pasta")


    fun onClick(){

    }
}