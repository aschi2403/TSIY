package aschi2403.tsiy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PowerActivityType(
    @PrimaryKey(autoGenerate = true)
    override var id: Long? = null,
    override var name: String = "",
    override var icon: String = "",
    override var description: String = ""
) : IActivityType