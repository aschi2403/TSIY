package aschi2403.tsiy.repository

import android.content.Context
import androidx.lifecycle.LiveData
import aschi2403.tsiy.db.*
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.model.PowerActivityType

class WorkoutRepo(context: Context) {
    private var db = WorkoutDatabase.getInstance(context)
    private var powerActivityDao: PowerActivityDao = db.powerActivityDao()
    private var powerActivityTypeDao: PowerActivityTypeDao = db.powerActivityTypeDao()
    private var activityTypeDao: ActivityTypeDao = db.activityTypeDao()
    private var generalActivityDao: GeneralActivityDao = db.generalActivityDao()


    // PowerActivity

    fun addPowerActivity(powerActivity: PowerActivity): Long? {
        val newId = powerActivityDao.insertPowerActivity(powerActivity)
        powerActivity.id = newId
        return newId
    }

    fun createPowerActivity() = PowerActivity()

    val allPowerActivities: /*LiveData<*/List<PowerActivity>
        /*>*/
        get() {
            return powerActivityDao.loadAll()
        }

    fun powerActivityById(id: Long) = powerActivityDao.loadPowerActivity(id)

    // GeneralActivity

    fun addGeneralActivity(generalActivity: GeneralActivity): Long? {
        val newId = generalActivityDao.insertGeneralActivity(generalActivity)
        generalActivity.id = newId
        return newId
    }

    fun createGeneralActivity() = GeneralActivity()

    val allGeneralActivities: List<GeneralActivity>
        get() {
            return generalActivityDao.loadAll()
        }

    fun generalActivityById(id: Long) = generalActivityDao.loadGeneralActivity(id)

    // PowerActivityType

    fun addPowerActivityType(powerActivityType: PowerActivityType): Long? {
        val newId = powerActivityTypeDao.insertPowerActivityType(powerActivityType)
        powerActivityType.id = newId
        return newId
    }

    fun createPowerActivityType() = PowerActivityType()

    val allPowerActivityTypes: LiveData<List<PowerActivityType>>
        get() {
            return powerActivityTypeDao.loadAll()
        }

    fun powerActivityTypeById(id: Long) = powerActivityTypeDao.loadPowerActivityType(id)

    // ActivityType
    fun addActivityType(activityType: ActivityType): Long? {
        val newId = activityTypeDao.insertActivityType(activityType)
        activityType.id = newId
        return newId
    }

    fun createActivityType() = ActivityType()

    val allActivityTypes: List<ActivityType>
        get() {
            return activityTypeDao.loadAll()
        }

    fun activityTypeById(id: Long) = activityTypeDao.loadActivityType(id)

}