package aschi2403.tsiy.model

import aschi2403.tsiy.model.relations.IActivity

class DoneWorkout(
    override var id: Long? = null,
) : IActivity {
    override var activityTypeId: Long = 0
    override var workoutId: Int = 0
    override var workoutPlanId: Long = 0
    override lateinit var activityType: ActivityType
    override var startDate: Long = 0
    override var endDate: Long = 0
    override var duration: Long = 0
    override var cardioPoints: Double = 0.0
    override var calories: Double = 0.0

    override fun toString(): String {
        return "DoneWorkout(id=$id, startDate=$startDate, endDate=$endDate, activityTypeId=$activityTypeId, " +
                "duration=$duration, cardioPoints=$cardioPoints, calories=$calories, " +
                "workoutId=$workoutId, workoutPlanId=$workoutPlanId, activityType=$activityType)"
    }

}
