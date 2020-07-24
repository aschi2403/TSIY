package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ActivityType(
    @PrimaryKey(autoGenerate = true)
    override var id: Long? = 0,
    override var name: String = "",
    override var icon: String = "",
    override var description: String = ""
) : IActivityType {
    override fun toString(): String {
        return "ActivityType(id=$id, name='$name', icon='$icon', description='$description')"
    }
}