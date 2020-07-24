package aschi2403.tsiy.model

interface IActivityType {
    var id: Long?
    var name: String
    var icon: String
    var description: String

    override fun toString(): String
}