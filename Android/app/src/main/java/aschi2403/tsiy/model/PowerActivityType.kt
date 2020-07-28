package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("id")])
class PowerActivityType(
    @PrimaryKey(autoGenerate = true)
    override var id: Long? = null,
    override var name: String = "",
    override var icon: String = "",
    override var description: String = ""
) : IActivityType {

    override fun toString(): String {
        return "PowerActivityType(id=$id, name='$name', icon='$icon', description='$description')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PowerActivityType

        if (id != other.id) return false
        if (name != other.name) return false
        if (icon != other.icon) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }


}