package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "WorkoutSession",
    foreignKeys = [
        ForeignKey(
            entity = Activity::class,
            parentColumns = ["id"],
            childColumns = ["idOfActivity"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WorkoutPlan::class,
            parentColumns = ["id"],
            childColumns = ["idOfWorkoutPlan"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index("id")])
class WorkoutSession(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var idOfActivity: Long,
    var idOfWorkoutPlan: Long,
    var idForMerging: Long
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkoutSession

        if (id != other.id) return false
        if (idOfActivity != other.idOfActivity) return false
        if (idOfWorkoutPlan != other.idOfWorkoutPlan) return false
        if (idForMerging != other.idForMerging) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + idOfActivity.hashCode()
        result = 31 * result + idOfWorkoutPlan.hashCode()
        result = 31 * result + idForMerging.hashCode()
        return result
    }

    override fun toString(): String {
        return "WorkoutSession(id=$id, idOfActivity=$idOfActivity, idOfWorkoutPlan=$idOfWorkoutPlan, idForMerging=$idForMerging)"
    }
}
