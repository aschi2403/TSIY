package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeightEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var date: Long = 0,
    var weight: Double = 0.0
)