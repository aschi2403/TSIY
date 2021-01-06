package aschi2403.tsiy.model

import androidx.room.*
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
    var repetitions: Int = 0,
    var weight: Double = 0.0
) : IActivity {
    @Ignore
    lateinit var powerActivityType: ActivityType

    override fun toString(): String {
        return "PowerActivity(id=$id, activityTypeId=$activityTypeId, repetitions=$repetitions, weight=$weight)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PowerActivity

        if (id != other.id) return false
        if (activityTypeId != other.activityTypeId) return false
        if (repetitions != other.repetitions) return false
        if (weight != other.weight) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + activityTypeId.hashCode()
        result = 31 * result + repetitions
        result = 31 * result + weight.hashCode()
        return result
    }


}