package de.fherfurt.onlyoneegg.view.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.fherfurt.onlyoneegg.interfaceCustom.ViewModelCustom
import de.fherfurt.onlyoneegg.storage.CookbookRepository
import de.fherfurt.onlyoneegg.storage.RecipeRepository

/*
* DashboardViewModel class tracks all cookbooks and uses cookbookRepository
* */
class DashboardViewModel(
    application: Application, cookbookRepository: CookbookRepository,
    recipeRepository: RecipeRepository
) : AndroidViewModel(application), ViewModelCustom {

    val cookbookRepository = cookbookRepository
    val recipeRepository = recipeRepository
    val cookbooks = cookbookRepository.getAllCookbooks()

    fun removeAllSelectedCookbooks(ids: MutableList<Long>) {
        ids.forEach { id ->
            removeCookbook(id, recipeRepository, cookbookRepository);

        }
    }
}