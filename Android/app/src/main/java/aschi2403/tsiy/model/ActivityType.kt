package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import aschi2403.tsiy.model.relations.IActivityType

@Entity(tableName = "ActivityType", indices = [Index("id")])
class ActivityType(
    @PrimaryKey(autoGenerate = true)
    override var id: Long? = 0,
    override var name: String = "",
    override var icon: Int = 0,
    override var description: String = "",
    override var isPowerActivity: Boolean,
    override var caloriesPerMinute: Double,
    override var cardioPointsPerMinute: Double
) : IActivityType {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ActivityType

        if (id != other.id) return false
        if (name != other.name) return false
        if (icon != other.icon) return false
        if (description != other.description) return false
        if (isPowerActivity != other.isPowerActivity) return false
        if (caloriesPerMinute != other.caloriesPerMinute) return false
        if (cardioPointsPerMinute != other.cardioPointsPerMinute) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + isPowerActivity.hashCode()
        result = 31 * result + caloriesPerMinute.hashCode()
        result = 31 * result + cardioPointsPerMinute.hashCode()
        return result
    }

    override fun toString(): String {
        return "ActivityType(id=$id, name='$name', icon='$icon', description='$description+', " +
                "powerActivity=$isPowerActivity, caloriesPerMinute=$caloriesPerMinute, " +
                "cardioPointsPerMinute=$cardioPointsPerMinute)"
    }

}
