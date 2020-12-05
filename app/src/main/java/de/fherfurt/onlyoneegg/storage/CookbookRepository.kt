package de.fherfurt.onlyoneegg.storage

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import de.fherfurt.onlyoneegg.model.Cookbook


class CookbookRepository(private val cookbookDao: CookbookDao) {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(cookbook: Cookbook) {
        cookbookDao.insert(cookbook)
    }


     fun getAllCookbooks():LiveData<List<Cookbook>> {
        return cookbookDao.getAllCookbooks()
    }
}
