package de.fherfurt.onlyoneegg.view.ui.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import kotlinx.coroutines.launch


class DashboardViewModel(application: Application, cookbookRepository: CookbookRepository): AndroidViewModel(application) {

    val cookbookRepository=cookbookRepository
    val cookbooks=cookbookRepository.getAllCookbooks()
    // here repository reference
    // get contacts form repository als livedata

    val cookbookTest=Cookbook()
    val cookbookDao = OOEDatabase.getInstance(application).cookbookDao;




    fun onClick(){
        cookbookTest.cookbookName = "Salads"

        viewModelScope.launch {
            val salads = cookbookDao.insert(cookbookTest) ?: return@launch
            val desserts=Cookbook()
            desserts.cookbookName="Desserts"
            cookbookDao.insert(desserts)

        }

    }


}