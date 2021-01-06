package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import aschi2403.tsiy.model.ActivityType

@Dao
interface PowerActivityTypeDao {
    @Query("SELECT * FROM ActivityType WHERE powerActivity = 1")
    fun loadAll(): List<ActivityType>

    @Query("SELECT * FROM ActivityType WHERE id=:powerActivityTypeId AND powerActivity = 1")
    fun loadPowerActivityType(powerActivityTypeId: Long): ActivityType

    @Query("SELECT * FROM ActivityType WHERE id=:powerActivityTypeId AND powerActivity = 1")
    fun loadLivePowerActivityType(powerActivityTypeId: Long): LiveData<ActivityType>
}