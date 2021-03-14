package aschi2403.tsiy.model

import androidx.room.*
import aschi2403.tsiy.model.relations.IActivity
import java.util.*

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
    override var time: Long = 0,
    override var cardioPoints: Double = 0.0,
    var calories: Double = 0.0,
    override var date: Long = 0
) : IActivity {
    @Ignore
    lateinit var powerActivityType: ActivityType
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PowerActivity

        if (id != other.id) return false
        if (activityTypeId != other.activityTypeId) return false
        if (sets != other.sets) return false
        if (time != other.time) return false
        if (cardioPoints != other.cardioPoints) return false
        if (calories != other.calories) return false
        if (date != other.date) return false
        if (powerActivityType != other.powerActivityType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + activityTypeId.hashCode()
        result = 31 * result + sets
        result = 31 * result + time.hashCode()
        result = 31 * result + cardioPoints.hashCode()
        result = 31 * result + calories.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + powerActivityType.hashCode()
        return result
    }

    override fun toString(): String {
        return "PowerActivity(id=$id, activityTypeId=$activityTypeId, sets=$sets, time=$time, cardioPoints=$cardioPoints, calories=$calories, date=$date"
    }
}