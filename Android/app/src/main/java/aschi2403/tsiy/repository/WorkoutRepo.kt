package aschi2403.tsiy.repository

import android.content.Context
import androidx.lifecycle.LiveData
import aschi2403.tsiy.db.PowerActivityDao
import aschi2403.tsiy.db.PowerActivityTypeDao
import aschi2403.tsiy.db.WorkoutDatabase
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.model.PowerActivityType

class WorkoutRepo(context: Context) {
    private var db = WorkoutDatabase.getInstance(context)
    private var powerActivityDao: PowerActivityDao = db.powerActivityDao()
    private var powerActivityTypeDao: PowerActivityTypeDao = db.powerActivityTypeDao()


    // PowerActivity

    fun addPowerActivity(powerActivity: PowerActivity): Long? {
        val newId = powerActivityDao.insertPowerActivity(powerActivity)
        powerActivity.id = newId
        return newId
    }

    fun createPowerActivity(): PowerActivity {
        return PowerActivity()
    }

    val allPowerActivities: LiveData<List<PowerActivity>>
        get() {
            return powerActivityDao.loadAll()
        }


    // PowerActivityType

    fun addPowerActivityType(powerActivityType: PowerActivityType): Long? {
        val newId = powerActivityTypeDao.insertPowerActivityType(powerActivityType)
        powerActivityType.id = newId
        return newId
    }

    fun createPowerActivityType(): PowerActivityType {
        return PowerActivityType()
    }

    val allPowerActivityTypes: LiveData<List<PowerActivityType>>
        get() {
            return powerActivityTypeDao.loadAll()
        }

}