package aschi2403.tsiy.model.relations

import aschi2403.tsiy.model.ActivityType

interface IActivity {
    var id: Long?
    var startDate: Long
    var endDate: Long
    var activityTypeId: Long
    var duration: Long
    var cardioPoints: Double
    var calories: Double
    var workoutId: Int
    var workoutPlanId: Long

    var activityType: ActivityType
    override fun toString(): String
}
