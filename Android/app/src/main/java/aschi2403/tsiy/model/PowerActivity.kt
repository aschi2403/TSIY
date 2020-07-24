package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PowerActivity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long? = null,
    override var name: String = "",
    var repetitions: Int = 0,
    var weight: Double = 0.0
) : IActivity {
    override fun toString(): String {
        return "PowerActivity(id=$id, name='$name', repetitions=$repetitions, weight=$weight)"
    }
}