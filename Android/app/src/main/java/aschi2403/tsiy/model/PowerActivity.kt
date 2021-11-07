package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import aschi2403.tsiy.model.relations.IActivity

@Entity(
    tableName = "PowerActivity",
    foreignKeys = [
        ForeignKey(
            entity = ActivityType::class,
            parentColumns = ["id"],
            childColumns = ["activityTypeId"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index("activityTypeId")]
)
class PowerActivity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long? = null,
    override var activityTypeId: Long = 0,
    var sets: Int = 0,
    override var duration: Long = 0,
    override var cardioPoints: Double = 0.0,
    override var calories: Double = 0.0,
    override var startDate: Long = 0,
    override var endDate: Long = 0,
    override var workoutId: Int = -1,
    override var workoutPlanId: Long = -1
) : IActivity {
    @Ignore
    override lateinit var activityType: ActivityType

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PowerActivity

        if (id != other.id) return false
        if (activityTypeId != other.activityTypeId) return false
        if (sets != other.sets) return false
        if (duration != other.duration) return false
        if (cardioPoints != other.cardioPoints) return false
        if (calories != other.calories) return false
        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false
        if (activityType != other.activityType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + activityTypeId.hashCode()
        result = 31 * result + sets
        result = 31 * result + duration.hashCode()
        result = 31 * result + cardioPoints.hashCode()
        result = 31 * result + calories.hashCode()
        result = 31 * result + startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        result = 31 * result + activityType.hashCode()
        return result
    }

    override fun toString(): String {
        return "PowerActivity(id=$id, activityTypeId=$activityTypeId, sets=$sets, time=$duration, " +
                "cardioPoints=$cardioPoints, calories=$calories, date=$startDate, pause=$endDate, " +
                "powerActivityType=$activityType)"
    }
}
