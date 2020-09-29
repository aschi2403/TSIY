package aschi2403.tsiy.model.relations

interface IActivity {
    var id: Long?
    var activityTypeId: Long

    override fun toString(): String
}