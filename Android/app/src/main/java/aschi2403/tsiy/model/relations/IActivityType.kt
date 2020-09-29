package aschi2403.tsiy.model.relations

interface IActivityType {
    var id: Long?
    var name: String
    var icon: String
    var description: String

    override fun toString(): String
}