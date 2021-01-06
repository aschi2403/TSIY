package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import aschi2403.tsiy.model.ActivityType

@Dao
interface ActivityTypeDao {
    @Query("SELECT * FROM ActivityType WHERE powerActivity = 0")
    fun loadAll(): List<ActivityType>

    @Query("SELECT * FROM ActivityType WHERE id=:activityTypeId AND powerActivity = 0")
    fun loadActivityType(activityTypeId: Long): ActivityType

    @Query("SELECT * FROM ActivityType WHERE id=:activityTypeId AND powerActivity = 0")
    fun loadLiveActivityType(activityTypeId: Long): LiveData<ActivityType>

    @Insert(onConflict = IGNORE)
    fun insertActivityType(activityType: ActivityType): Long

    @Update(onConflict = REPLACE)
    fun updateActivityType(activityType: ActivityType)

    @Delete
    fun deleteActivityType(activityType: ActivityType)
}