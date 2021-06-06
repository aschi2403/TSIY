package aschi2403.tsiy.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import aschi2403.tsiy.model.relations.IActivity

@Entity(
    tableName = "GeneralActivity",
    foreignKeys = [
        ForeignKey(
            entity = ActivityType::class,
            parentColumns = ["id"],
            childColumns = ["activityTypeId"],
            onDelete = CASCADE
        )],
    indices = [Index("activityTypeId")]
)
class GeneralActivity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long? = null,
    override var activityTypeId: Long = 0,
    // time in milliseconds from start
    override var duration: Long = 0,
    override var cardioPoints: Double = 0.0,
    override var calories: Double = 0.0,
    override var startDate: Long = 0,
    override var endDate: Long = 0,
    var distance: Float = 0F
) : IActivity {
    @Ignore
    lateinit var activityType: ActivityType
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GeneralActivity

        if (id != other.id) return false
        if (activityTypeId != other.activityTypeId) return false
        if (duration != other.duration) return false
        if (cardioPoints != other.cardioPoints) return false
        if (calories != other.calories) return false
        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false
        if (distance != other.distance) return false
        if (activityType != other.activityType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + activityTypeId.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + cardioPoints.hashCode()
        result = 31 * result + calories.hashCode()
        result = 31 * result + startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        result = 31 * result + distance.hashCode()
        result = 31 * result + activityType.hashCode()
        return result
    }

    override fun toString(): String {
        return "GeneralActivity(id=$id, activityTypeId=$activityTypeId, duration=$duration, cardioPoints=$cardioPoints, calories=$calories, startDate=$startDate, endDate=$endDate, distance=$distance, activityType=$activityType)"
    }

}