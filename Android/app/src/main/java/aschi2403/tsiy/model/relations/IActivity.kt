package aschi2403.tsiy.model.relations

interface IActivity {
    var id: Long?
    var startDate: Long
    var endDate: Long
    var activityTypeId: Long
    var duration: Long
    var cardioPoints: Double
    var calories: Double

    override fun toString(): String
}