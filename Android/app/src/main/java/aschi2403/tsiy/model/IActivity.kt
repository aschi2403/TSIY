package aschi2403.tsiy.model

interface IActivity {
    var id: Long?
    var activityTypeId: Long

    override fun toString(): String
}