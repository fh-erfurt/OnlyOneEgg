package de.fherfurt.onlyoneegg.view.ui.inputCookbook

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import kotlinx.coroutines.launch

/*
* DashboardViewModel uses cookbookRepository to insert new cookbooks
* */
class InputCookbookViewModel(application: Application, cookbookRepository: CookbookRepository): AndroidViewModel(application) {

    val cookbookRepository=cookbookRepository



    fun insertCookbook(cookbook: Cookbook){
        viewModelScope.launch {
            val id= cookbookRepository.insert(cookbook)
            Log.i("InputCookbookViewModel", "Inserted cookbook with id $id")
        }
    }

    fun getLastCookbookId(): Long {
        return cookbookRepository.getLastCookbookId()
    }


}