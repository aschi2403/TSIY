package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "WorkoutPlan", indices = [Index("id")])
class WorkoutPlan(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkoutPlan

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "WorkoutPlan(id=$id, name='$name')"
    }
}
