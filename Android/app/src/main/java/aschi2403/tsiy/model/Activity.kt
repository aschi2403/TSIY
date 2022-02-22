package aschi2403.tsiy.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import aschi2403.tsiy.model.relations.IActivity

@Entity(
    tableName = "Activity",
    foreignKeys = [
        ForeignKey(
            entity = ActivityType::class,
            parentColumns = ["id"],
            childColumns = ["activityTypeId"],
            onDelete = CASCADE
        )],
    indices = [Index("activityTypeId")]
)
open class Activity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long? = null,
    override var activityTypeId: Long = 0,
    override var duration: Long = 0,
    override var startDate: Long = 0,
    override var endDate: Long = 0
) : IActivity {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Activity

        if (id != other.id) return false
        if (activityTypeId != other.activityTypeId) return false
        if (duration != other.duration) return false
        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + activityTypeId.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        return result
    }

    override fun toString(): String {
        return "Activity(id=$id, activityTypeId=$activityTypeId, duration=$duration, " +
                "startDate=$startDate, endDate=$endDate"
    }
}
