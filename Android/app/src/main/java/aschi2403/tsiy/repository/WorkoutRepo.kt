package aschi2403.tsiy.repository

import android.content.Context
import aschi2403.tsiy.db.*
import aschi2403.tsiy.model.*

class WorkoutRepo(context: Context) {
    private var db = WorkoutDatabase.getInstance(context)
    private var powerActivityDao: PowerActivityDao = db.powerActivityDao()
    private var powerActivityTypeDao: PowerActivityTypeDao = db.powerActivityTypeDao()
    private var activityTypeDao: ActivityTypeDao = db.activityTypeDao()
    private var generalActivityDao: GeneralActivityDao = db.generalActivityDao()
    private var weightEntryDao: WeightEntryDao = db.weightEntryDao()
    private var setEntryDao: SetEntryDao = db.setEntryDao()
    private var workoutEntryDao: WorkoutEntryDao = db.workoutEntryDao()
    private var workoutPlanDao: WorkoutPlanDao = db.workoutPlanDao()


    // PowerActivity

    fun addPowerActivity(powerActivity: PowerActivity): Long {
        val newId = powerActivityDao.insertPowerActivity(powerActivity)
        powerActivity.id = newId
        return newId
    }

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

    fun updatePowerActivity(powerActivity: PowerActivity) {
        powerActivityDao.updatePowerActivity(powerActivity)
    }

    fun deletePowerActivity(powerActivity: PowerActivity) {
        powerActivityDao.deletePowerActivity(powerActivity)
    }

    // GeneralActivity

    fun addGeneralActivity(generalActivity: GeneralActivity): Long {
        val newId = generalActivityDao.insertGeneralActivity(generalActivity)
        generalActivity.id = newId
        return newId
    }

    fun updateActivity(generalActivity: GeneralActivity) {
        generalActivityDao.updateGeneralActivity(generalActivity)
    }

    val allGeneralActivities: List<GeneralActivity>
        get() {
            val list = generalActivityDao.loadAll()

            list.forEach {
                it.activityType = activityTypeById(it.activityTypeId)
            }

            return list
        }

    fun generalActivityById(id: Long) = generalActivityDao.loadGeneralActivity(id)

    fun deleteGeneralActivity(generalActivity: GeneralActivity) {
        generalActivityDao.deleteGeneralActivity(generalActivity)
    }
    // PowerActivityType

    val allPowerActivityTypes: List<ActivityType>
        get() {
            return powerActivityTypeDao.loadAll()
        }

    fun powerActivityTypeById(id: Long) = powerActivityTypeDao.loadPowerActivityType(id)

    // ActivityType

    fun addActivityType(activityType: ActivityType): Long {
        val newId = activityTypeDao.insertActivityType(activityType)
        activityType.id = newId
        return newId
    }

    fun updateActivityType(activityType: ActivityType) {
        activityTypeDao.updateActivityType(activityType)
    }

    fun deleteActivityType(activityType: ActivityType) {
        activityTypeDao.deleteActivityType(activityType)
    }


    val allActivityTypes: List<ActivityType>
        get() {
            return activityTypeDao.loadAll()
        }

    fun activityTypeById(id: Long) = activityTypeDao.loadActivityType(id)

    // WeightEntry
    fun addWeightEntry(weightEntry: WeightEntry): Long {
        val newId = weightEntryDao.insertWeightEntry(weightEntry)
        weightEntry.id = newId
        return newId
    }

    val allWeightEntries: List<WeightEntry>
        get() {
            return weightEntryDao.loadAll()
        }

    fun weightEntryById(id: Long) = weightEntryDao.loadWeightEntry(id)


    fun insertSetEntry(setEntry: SetEntry): Long {
        val newId = setEntryDao.insertSetEntry(setEntry)
        setEntry.id = newId
        return newId
    }

    fun getSetEntriesByPowerActivityId(powerActivityId: Long): List<SetEntry> {
        return setEntryDao.getSetEntriesByPowerActivityId(powerActivityId)
    }

    // WorkoutPlan

    val allWorkoutPlans: List<WorkoutPlan>
        get() {
            return workoutPlanDao.loadAll()
        }

    fun workoutPlanById(id: Long) = workoutPlanDao.loadWorkoutPlan(id)


    fun insertWorkoutPlan(workoutPlan: WorkoutPlan): Long {
        val newId = workoutPlanDao.insertWorkoutPlan(workoutPlan)
        workoutPlan.id = newId
        return newId
    }

    fun deleteWorkoutPlan(workoutPlan: WorkoutPlan) {
        workoutPlanDao.deleteWorkoutPlan(workoutPlan)
    }

    // WorkoutEntry

    val allWorkoutEntry: List<WorkoutEntry>
        get() {
            return workoutEntryDao.loadAll()
        }

    fun workoutEntryById(id: Long) = workoutPlanDao.loadWorkoutPlan(id)

    fun workoutEntriesByWorkoutPlanId(workoutPlanId: Long): List<WorkoutEntry> {
        return allWorkoutEntry
            .filter { workoutEntry -> workoutEntry.workoutPlanId == workoutPlanId }
    }

    fun updateWorkoutEntry(workoutEntry: WorkoutEntry) {
        workoutEntryDao.updateWorkoutEntry(workoutEntry)
    }

    fun insertWorkoutEntry(workoutEntry: WorkoutEntry): Long {
        val newId = workoutEntryDao.insertWorkoutEntry(workoutEntry)
        workoutEntry.id = newId
        return newId
    }

    fun deleteWorkoutEntry(workoutEntry: WorkoutEntry) {
        workoutEntryDao.deleteWorkoutEntry(workoutEntry)
    }
}