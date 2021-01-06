package aschi2403.tsiy.model.relations

interface IActivityType {
    var id: Long?
    var name: String
    var icon: String
    var description: String
    var powerActivity: Boolean

    override fun toString(): String
}