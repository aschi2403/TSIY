package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "SetEntry", foreignKeys = [
        ForeignKey(
            entity = PowerActivity::class,
            parentColumns = ["id"],
            childColumns = ["powerActivityId"],
            onDelete = ForeignKey.CASCADE
        )], indices = [Index("id")]
)
class SetEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var weight: Double = 0.0,
    var repetitions: Int = 0,
    var powerActivityId: Long = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SetEntry

        if (id != other.id) return false
        if (weight != other.weight) return false
        if (repetitions != other.repetitions) return false
        if (powerActivityId != other.powerActivityId) return false

        return true
    }

    override fun toString(): String {
        return "SetEntry(id=$id, weight=$weight, repetitions=$repetitions, powerActivityId=$powerActivityId)"
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + weight.hashCode()
        result = 31 * result + repetitions
        result = 31 * result + powerActivityId.hashCode()
        return result
    }
}
