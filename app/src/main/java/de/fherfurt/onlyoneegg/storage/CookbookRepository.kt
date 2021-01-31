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

    fun getLastCookbookId():Long{
       return cookbookDao.lastCookbookId()
    }
    fun getAllCookbooks(): LiveData<List<Cookbook>> {
        return cookbookDao.getAllCookbooks()
    }

    fun getCookbook(): Cookbook {
        return cookbookDao.getCookbook()
    }
    fun getCookbook(cookbookId: Long): Cookbook {
        return cookbookDao.getCookbook(cookbookId)
    }

     fun delete(cookbookId:Long){
         cookbookDao.delete(cookbookDao.getCookbook(cookbookId))
     }

}
