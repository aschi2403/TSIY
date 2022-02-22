package aschi2403.tsiy.helper

import aschi2403.tsiy.model.Activity
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.model.WorkoutSession
import aschi2403.tsiy.model.relations.ActivityWithCardioActivity
import aschi2403.tsiy.repository.WorkoutRepo

class DataMerger(private val repo: WorkoutRepo) {

    fun getData(checkedItem: Int, workoutPrefix: String): MutableList<ActivityWithCardioActivity> {
        return when (checkedItem) {
            0 -> {
                mergeByWorkouts(repo.allActivities, workoutPrefix)
            }
            1 -> {
                mergeByWorkouts(repo.allActivities.filter { entry -> entry.cardioActivity != null }, workoutPrefix)
            }
            else -> {
                mergeByWorkouts(repo.allActivities.filter { entry -> entry.cardioActivity == null }, workoutPrefix)
            }
        }
    }

    private fun mergeByWorkouts(
        list: List<ActivityWithCardioActivity>,
        workoutPrefix: String
    ): MutableList<ActivityWithCardioActivity> {
        val mergedList = mutableListOf<ActivityWithCardioActivity>()
        repo.allWorkoutSessions().groupBy { k -> k.idForMerging }
            .forEach { entry -> mergedList.add(getWorkoutInfoByActivities(entry.value, workoutPrefix)) }

        list.filter { activityWithCardioActivity ->
            isActivityNotInAWorkout(activityWithCardioActivity.activity.id)
        }.forEach { activityWithCardioActivity -> mergedList.add(activityWithCardioActivity) }

        return mergedList.sortedByDescending { it.activity.startDate }.toMutableList()
    }

    private fun isActivityNotInAWorkout(activityId: Long?) =
        repo.allWorkoutSessions()
            .find { entry -> entry.idOfActivity == activityId } == null

    private fun getWorkoutInfoByActivities(
        workoutSession: List<WorkoutSession>,
        workoutPrefix: String
    ): ActivityWithCardioActivity {
        val mergedWorkout = ActivityWithCardioActivity()
        mergedWorkout.activity = Activity()
        mergedWorkout.activity.startDate = workoutSession.minOf { a -> repo.getActivityById(a.idOfActivity).startDate }
        mergedWorkout.activity.endDate = workoutSession.maxOf { a -> repo.getActivityById(a.idOfActivity).endDate }
        mergedWorkout.activity.duration = workoutSession.sumOf { a -> repo.getActivityById(a.idOfActivity).duration }
        mergedWorkout.workoutId = workoutSession.first().idForMerging

        mergedWorkout.activityType = ActivityType(
            name = "$workoutPrefix ${repo.workoutPlanById(workoutSession.first().idOfWorkoutPlan).name}",
            icon = 1,
            caloriesPerMinute = 0.0,
            cardioPointsPerMinute = 0.0,
            description = "",
            isPowerActivity = false
        )
        return mergedWorkout
    }

    fun getDataFromWorkoutId(workoutId: Long): List<ActivityWithCardioActivity> {
        return repo.workoutSessionById(workoutId)
            .map { entry ->
                setWorkoutId(
                    repo.getActivityWithCardioActivityById(entry.idOfActivity),
                    workoutId
                )
            }
    }

    private fun setWorkoutId(
        activityWithCardioActivityById: ActivityWithCardioActivity,
        workoutId: Long
    ): ActivityWithCardioActivity {
        activityWithCardioActivityById.workoutId = workoutId
        return activityWithCardioActivityById
    }
}
