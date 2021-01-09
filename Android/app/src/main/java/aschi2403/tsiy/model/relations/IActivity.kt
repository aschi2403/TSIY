package aschi2403.tsiy.model.relations

interface IActivity {
    var id: Long?
    var date: Long
    var activityTypeId: Long
    var time: Long
    var cardioPoints: Double

    override fun toString(): String
}