package de.fherfurt.onlyoneegg.view.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import kotlinx.coroutines.launch


class DashboardViewModel(application: Application): AndroidViewModel(application) {


// here repository reference
    // get contacts form repository als livedata
    val book="lala"
    val cookbookTest=Cookbook()
    val cookbookDao = OOEDatabase.getInstance(application).cookbookDao;

    /*val cookbooks : List<String>
        get() = mutableListOf("Salads", "Desserts", "Pasta")
*/

    fun onInsertCookbook(){

    }




    fun onClick(){
        cookbookTest.cookbookName = "Salads"

        viewModelScope.launch {
            val salads = cookbookDao.insert(cookbookTest) ?: return@launch
        }
    }
}