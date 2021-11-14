package aschi2403.tsiy.helper

import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.model.DoneWorkout
import aschi2403.tsiy.model.relations.IActivity
import aschi2403.tsiy.repository.WorkoutRepo

private const val IS_IN_NO_WORKOUT = -1

class DataMerger(val database: WorkoutRepo) {

    fun getData(checkedItem: Int, workoutPrefix: String): MutableList<IActivity> {
        return when (checkedItem) {
            0 -> {
                mergeByWorkouts(database.allPowerActivities.plus(database.allGeneralActivities), workoutPrefix)
            }
            1 -> {
                mergeByWorkouts(database.allGeneralActivities, workoutPrefix)
            }
            else -> {
                mergeByWorkouts(database.allPowerActivities, workoutPrefix)
            }
        }
    }

    private fun mergeByWorkouts(list: List<IActivity>, workoutPrefix: String): MutableList<IActivity> {
        val mergedList = mutableListOf<IActivity>()
        val groupedList = list.groupBy { a -> a.workoutId }

        for (activity in groupedList) {
            if (activity.key == IS_IN_NO_WORKOUT || activity.value.size == 1) {
                mergedList.addAll(activity.value)
            } else {
                mergedList.add(getWorkoutInfoByActivities(activity.value, workoutPrefix))
            }
        }
        return mergedList.sortedByDescending { it.startDate }.toMutableList()
    }

    private fun getWorkoutInfoByActivities(activities: List<IActivity>, workoutPrefix: String): IActivity {
        val doneWorkout = DoneWorkout()
        doneWorkout.startDate = activities.minOf { a -> a.startDate }
        doneWorkout.endDate = activities.maxOf { a -> a.endDate }
        doneWorkout.cardioPoints = activities.sumByDouble { a -> a.cardioPoints }
        doneWorkout.duration = activities.sumOf { a -> a.duration }
        doneWorkout.workoutPlanId = activities[0].workoutPlanId
        doneWorkout.workoutId = activities[0].workoutId

        doneWorkout.activityType = ActivityType(
            name = "$workoutPrefix ${database.workoutPlanById(activities.first().workoutPlanId).name}",
            icon = 1,
            caloriesPerMinute = 0.0,
            cardioPointsPerMinute = 0.0,
            description = "",
            isPowerActivity = false
        )
        return doneWorkout
    }

    fun getDataFromWorkoutId(checkedItem: Int, workoutId: Int): MutableList<IActivity> {
        return when (checkedItem) {
            0 -> {
                database.allPowerActivities.filter { a -> a.workoutId == workoutId }
                    .plus(database.allGeneralActivities.filter { a -> a.workoutId == workoutId })
                    .toMutableList()
            }
            1 -> {
                database.allGeneralActivities.filter { a -> a.workoutId == workoutId }.toMutableList()
            }
            else -> {
                database.allPowerActivities.filter { a -> a.workoutId == workoutId }.toMutableList()
            }
        }
    }
}
