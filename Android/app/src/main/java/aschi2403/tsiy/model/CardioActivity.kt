package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Suppress("LongParameterList")
@Entity(
    tableName = "CardioActivity",
    foreignKeys = [
        ForeignKey(
            entity = Activity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )],
    ignoredColumns = ["activityTypeId", "duration", "startDate", "endDate"]
)
class CardioActivity(
    @PrimaryKey
    override var id: Long? = null,
    var distance: Float = 0F
) : Activity() {
    override var activityTypeId: Long = 0
    override var duration: Long = 0
    override var startDate: Long = 0
    override var endDate: Long = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardioActivity

        if (id != other.id) return false
        if (distance != other.distance) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + distance.hashCode()
        return result
    }

    override fun toString(): String {
        return "GeneralActivity(id=$id, distance=$distance"
    }
}
