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
}