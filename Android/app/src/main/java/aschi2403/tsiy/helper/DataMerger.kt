package aschi2403.tsiy.helper

import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.relations.IActivity
import aschi2403.tsiy.repository.WorkoutRepo

class DataMerger(val database: WorkoutRepo) {

    fun getData(checkedItem: Int, workoutName: String): MutableList<IActivity> {
        return when (checkedItem) {
            0 -> {
                mergeByWorkouts(database.allPowerActivities.plus(database.allGeneralActivities), workoutName)
            }
            1 -> {
                mergeByWorkouts(database.allGeneralActivities, workoutName)
            }
            else -> {
                mergeByWorkouts(database.allPowerActivities, workoutName)
            }
        }
    }

    private fun mergeByWorkouts(list: List<IActivity>, workoutName: String): MutableList<IActivity> {
        val mergedList = mutableListOf<IActivity>()
        val groupedList = list.groupBy { a -> a.workoutId }

        for (activity in groupedList) {
            if (activity.key == -1 || activity.value.size == 1)
                mergedList.addAll(activity.value)
            else
                mergedList.add(getWorkoutInfoByActivities(activity.value, workoutName))
        }
        return mergedList.sortedByDescending { it.startDate }.toMutableList()
    }

    private fun getWorkoutInfoByActivities(activities: List<IActivity>, workout: String): IActivity {
        val activity = GeneralActivity()
        activity.startDate = activities.minOf { a -> a.startDate }
        activity.endDate = activities.maxOf { a -> a.endDate }
        activity.cardioPoints = activities.sumByDouble { a -> a.cardioPoints }
        activity.duration = activities.sumOf { a -> a.duration }

        activity.activityType = ActivityType(
            name = "$workout ${database.workoutPlanById(activities.first().workoutPlanId).name}",
            icon = 1,
            caloriesPerMinute = 0.0,
            cardioPointsPerMinute = 0.0,
            description = "",
            isPowerActivity = false
        )
        return activity
    }

    fun getDataFromWorkoutId(checkedItem: Int, workoutPlanId: Long): MutableList<IActivity> {
        return when (checkedItem) {
            0 -> {
                database.allPowerActivities.filter { a -> a.workoutPlanId == workoutPlanId }
                    .plus(database.allGeneralActivities.filter { a -> a.workoutPlanId == workoutPlanId }).toMutableList()
            }
            1 -> {
                database.allGeneralActivities.filter { a -> a.workoutPlanId == workoutPlanId }.toMutableList()
            }
            else -> {
                database.allPowerActivities.filter { a -> a.workoutPlanId == workoutPlanId }.toMutableList()
            }
        }
    }
}