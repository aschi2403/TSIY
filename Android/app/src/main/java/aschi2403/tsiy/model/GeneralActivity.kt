package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
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
    var time: Long = 0,
    var cardioPoints: Double,
    var calories: Double
) : IActivity {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GeneralActivity

        if (id != other.id) return false
        if (activityTypeId != other.activityTypeId) return false
        if (time != other.time) return false
        if (cardioPoints != other.cardioPoints) return false
        if (calories != other.calories) return false

        return true
    }


    override fun toString(): String {
        return "GeneralActivity(id=$id, activityTypeId=$activityTypeId, time=$time, cardioPoints=$cardioPoints, calories=$calories)"
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + activityTypeId.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + cardioPoints.hashCode()
        result = 31 * result + calories.hashCode()
        return result
    }

}