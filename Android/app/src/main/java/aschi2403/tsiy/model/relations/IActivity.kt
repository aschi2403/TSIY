package aschi2403.tsiy.model.relations

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

    override fun toString(): String
}