package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import aschi2403.tsiy.model.ActivityType

@Dao
interface AllActivityTypeDao {
    @Query("SELECT * FROM ActivityType")
    fun loadAll(): List<ActivityType>

    @Query("SELECT * FROM ActivityType WHERE id=:activityTypeId")
    fun loadActivityType(activityTypeId: Long): ActivityType

    @Query("SELECT * FROM ActivityType WHERE id=:activityTypeId")
    fun loadLiveActivityType(activityTypeId: Long): LiveData<ActivityType>

    @Insert(onConflict = IGNORE)
    fun insertActivityType(activityType: ActivityType): Long

    @Update(onConflict = REPLACE)
    fun updateActivityType(activityType: ActivityType)

    @Delete
    fun deleteActivityType(activityType: ActivityType)
}
