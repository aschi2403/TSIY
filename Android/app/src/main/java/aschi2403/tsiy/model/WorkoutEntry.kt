package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "WorkoutEntry", foreignKeys = [
        ForeignKey(
            entity = WorkoutPlan::class,
            parentColumns = ["id"],
            childColumns = ["workoutPlanId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ActivityType::class,
            parentColumns = ["id"],
            childColumns = ["iActivityTypeId"],
            onDelete = ForeignKey.CASCADE
        )
    ], indices = [Index("id")]
)
class WorkoutEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var workoutPlanId: Long,
    var iActivityTypeId: Long,
    var position: Int,
    val isPowerActivity: Boolean,
    var repetitions: Int = 3
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkoutEntry

        if (id != other.id) return false
        if (workoutPlanId != other.workoutPlanId) return false
        if (iActivityTypeId != other.iActivityTypeId) return false
        if (position != other.position) return false
        if (isPowerActivity != other.isPowerActivity) return false
        if(repetitions != other.repetitions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + workoutPlanId.hashCode()
        result = 31 * result + iActivityTypeId.hashCode()
        result = 31 * result + position
        result = 31 * result + isPowerActivity.hashCode()
        result = 31 * result + repetitions
        return result
    }

    override fun toString(): String {
        return "WorkoutEntry(id=$id, workoutPlanId=$workoutPlanId, iActivityTypeId=$iActivityTypeId, " +
                "position=$position, isPowerActivity=$isPowerActivity, repetitionsOfWorkout=$repetitions)"
    }
}
