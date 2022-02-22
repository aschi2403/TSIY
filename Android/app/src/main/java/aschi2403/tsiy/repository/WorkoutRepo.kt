package aschi2403.tsiy.repository

import android.content.Context
import android.text.BoringLayout
import aschi2403.tsiy.db.*
import aschi2403.tsiy.model.*
import aschi2403.tsiy.model.relations.ActivityWithCardioActivity
import aschi2403.tsiy.model.relations.IActivity

@Suppress("TooManyFunctions")
class WorkoutRepo(context: Context) {
    private var db = WorkoutDatabase.getInstance(context)
    private var activityDao: ActivityDao = db.activityDao()
    private var powerActivityTypeDao: PowerActivityTypeDao = db.powerActivityTypeDao()
    private var cardioActivityTypeDao: CardioActivityTypeDao = db.activityTypeDao()
    private var allActivityTypeDao: AllActivityTypeDao = db.allActivityTypeDao()
    private var cardioActivityDao: CardioActivityDao = db.generalActivityDao()
    private var weightEntryDao: WeightEntryDao = db.weightEntryDao()
    private var setEntryDao: SetEntryDao = db.setEntryDao()
    private var workoutPlanEntryDao: WorkoutPlanEntryDao = db.workoutPlanEntryDao()
    private var workoutPlanDao: WorkoutPlanDao = db.workoutPlanDao()
    private var gpsPointsDao: GPSPointsDao = db.gpsPointDao()
    private var workoutSessionDao: WorkoutSessionDao = db.getWorkoutSessionDao()


    fun addActivity(activity: Activity): Long {
        val newId = activityDao.insertActivity(activity)
        activity.id = newId
        return newId
    }

    val allActivities: List<ActivityWithCardioActivity>
        get() {
            return activityDao.loadAll()
        }

    fun getActivityWithCardioActivityById(activityId: Long): ActivityWithCardioActivity {
        return activityDao.getActivityWithCardioActivity(activityId)
    }

    fun getActivityById(activityId: Long): Activity {
        return activityDao.loadActivity(activityId)
    }

    fun updateActivity(activity: Activity) {
        activityDao.updateActivity(activity)
    }

    // CardioActivity

    fun addCardioActivity(cardioActivity: CardioActivity): Long {
        val newId = cardioActivityDao.insertCardioActivity(cardioActivity)
        cardioActivity.id = newId
        return newId
    }

    fun updateActivity(cardioActivity: CardioActivity) {
        cardioActivityDao.updateCardioActivity(cardioActivity)
    }

    val allCardioActivities: List<CardioActivity>
        get() {
            return cardioActivityDao.loadAll()
        }

    fun cardioActivityById(id: Long) = cardioActivityDao.loadCardioActivity(id)

// All ActivityType

    val allActivityTypes: List<ActivityType>
        get() {
            return allActivityTypeDao.loadAll()
        }

    fun allActivityTypeById(id: Long) = allActivityTypeDao.loadActivityType(id)

// PowerActivityType

    val allPowerActivityTypes: List<ActivityType>
        get() {
            return powerActivityTypeDao.loadAll()
        }

    fun powerActivityTypeById(id: Long) = powerActivityTypeDao.loadPowerActivityType(id)

// ActivityType

    fun addActivityType(activityType: ActivityType): Long {
        val newId = cardioActivityTypeDao.insertActivityType(activityType)
        activityType.id = newId
        return newId
    }

    fun updateActivityType(activityType: ActivityType) {
        cardioActivityTypeDao.updateActivityType(activityType)
    }

    fun deleteActivityType(activityType: ActivityType) {
        cardioActivityTypeDao.deleteActivityType(activityType)
    }


    val allGeneralActivityTypes: List<ActivityType>
        get() {
            return cardioActivityTypeDao.loadAll()
        }

    fun cardioActivityTypeById(id: Long) = cardioActivityTypeDao.loadActivityType(id)

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

    val allWorkoutEntry: List<WorkoutPlanEntry>
        get() {
            return workoutPlanEntryDao.loadAll()
        }

    fun workoutEntryById(id: Long) = workoutPlanDao.loadWorkoutPlan(id)

    fun workoutEntriesByWorkoutPlanId(workoutPlanId: Long): List<WorkoutPlanEntry> = allWorkoutEntry
        .filter { workoutEntry -> workoutEntry.workoutPlanId == workoutPlanId }

    fun updateWorkoutEntry(workoutEntry: WorkoutPlanEntry) =
        workoutPlanEntryDao.updateWorkoutEntry(workoutEntry)

    fun insertWorkoutEntry(workoutEntry: WorkoutPlanEntry): Long {
        val newId = workoutPlanEntryDao.insertWorkoutEntry(workoutEntry)
        workoutEntry.id = newId
        return newId
    }

    fun deleteWorkoutEntry(workoutEntry: WorkoutPlanEntry) =
        workoutPlanEntryDao.deleteWorkoutEntry(workoutEntry)

    fun insertGPSPoints(gpsPoints: List<GPSPoint>) =
        gpsPoints.forEach {
            gpsPointsDao.insertGPSPoint(it)
        }

    fun getGPSPointsFromActivity(activityId: Long): List<GPSPoint> = gpsPointsDao.loadGPSPoints(activityId)

    fun addActivity(activity: IActivity): Long {
        if (activity is CardioActivity) {
            return addCardioActivity(activity)
        }
        return addActivity(activity as Activity)
    }

    fun isPowerActivity(activityTypeId: Long): Boolean {
        return allActivityTypeById(activityTypeId).isPowerActivity
    }

    fun deleteActivity(activity: Activity) {
        activityDao.deleteActivity(activity)
    }

    fun allWorkoutSessions(): List<WorkoutSession> {
        return workoutSessionDao.loadAll()
    }

    fun insertWorkoutSession(workoutSession: WorkoutSession){
        workoutSessionDao.insertWorkoutSession(workoutSession)
    }

    fun workoutSessionById(workoutId: Long): List<WorkoutSession> {
       return workoutSessionDao.getWorkoutByMergingId(workoutId)
    }
    
    fun isWorkoutPlanInWorkoutSession(workoutId: Long): Boolean{
        return allWorkoutSessions().find { entry -> entry.idOfWorkoutPlan == workoutId } != null
    }

    fun deleteWorkoutSessionWithActivities(workoutId: Long) {
        workoutSessionDao.getWorkoutByMergingId(workoutId).forEach{entry -> activityDao.deleteActivityById(entry.idOfActivity)}
        workoutSessionDao.deleteWorkoutSessionByMergingId(workoutId)
    }
}
