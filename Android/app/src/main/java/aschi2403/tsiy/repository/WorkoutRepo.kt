package aschi2403.tsiy.repository

import android.content.Context
import androidx.lifecycle.LiveData
import aschi2403.tsiy.db.*
import aschi2403.tsiy.model.*

class WorkoutRepo(context: Context) {
    private var db = WorkoutDatabase.getInstance(context)
    private var powerActivityDao: PowerActivityDao = db.powerActivityDao()
    private var powerActivityTypeDao: PowerActivityTypeDao = db.powerActivityTypeDao()
    private var activityTypeDao: ActivityTypeDao = db.activityTypeDao()
    private var generalActivityDao: GeneralActivityDao = db.generalActivityDao()
    private var weightEntryDao: WeightEntryDao = db.weightEntryDao()


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
            val list = powerActivityDao.loadAll()

            list.forEach {
                it.powerActivityType = powerActivityTypeById(it.activityTypeId)
            }

            return list
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


    val allPowerActivityTypes: List<ActivityType>
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

    fun updateActivityType(activityType: ActivityType){
        activityTypeDao.updateActivityType(activityType);
    }


    val allActivityTypes: List<ActivityType>
        get() {
            return activityTypeDao.loadAll()
        }

    fun activityTypeById(id: Long) = activityTypeDao.loadActivityType(id)

    // WeightEntry
    fun addWeightEntry(weightEntry: WeightEntry): Long? {
        val newId = weightEntryDao.insertWeightEntry(weightEntry)
        weightEntry.id = newId
        return newId
    }

    fun createWeightEntry() = WeightEntry()

    val allWeightEntries: List<WeightEntry>
        get() {
            return weightEntryDao.loadAll()
        }

    fun weightEntryById(id: Long) = weightEntryDao.loadWeightEntry(id)
}