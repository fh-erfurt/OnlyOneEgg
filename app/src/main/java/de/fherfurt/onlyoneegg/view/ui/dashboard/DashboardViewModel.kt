package de.fherfurt.onlyoneegg.view.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.fherfurt.onlyoneegg.model.Cookbook
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.OOEDatabase
import kotlinx.coroutines.launch


class DashboardViewModel(application: Application, cookbookRepository: CookbookRepository): AndroidViewModel(application) {

    val cookbookRepository=cookbookRepository
    val cookbooks=cookbookRepository.getAllCookbooks()


}