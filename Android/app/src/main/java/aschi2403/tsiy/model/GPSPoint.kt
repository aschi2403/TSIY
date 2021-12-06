package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "GPSPoints",
    indices = [Index("workoutEntryId")]
)
class GPSPoint(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var latitude: Double,
    var longitude: Double,
    var workoutEntryId: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GPSPoint

        if (id != other.id) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (workoutEntryId != other.workoutEntryId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + workoutEntryId.hashCode()
        return result
    }

    override fun toString(): String {
        return "GPSPoints(id=$id, latitude=$latitude, longitude=$longitude, workoutEntryId=$workoutEntryId)"
    }
}
