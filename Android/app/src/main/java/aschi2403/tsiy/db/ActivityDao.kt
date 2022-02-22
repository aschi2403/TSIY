package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy.REPLACE
import aschi2403.tsiy.model.Activity
import aschi2403.tsiy.model.relations.ActivityWithCardioActivity

@Dao
interface ActivityDao {
    @Query("SELECT * FROM Activity")
    fun loadAll():List<ActivityWithCardioActivity>

    @Query("SELECT * FROM Activity WHERE id= :activityId")
    fun getActivityWithCardioActivity(activityId: Long): ActivityWithCardioActivity

    @Query("SELECT * FROM Activity WHERE id= :activityId")
    fun loadActivity(activityId: Long): Activity

    @Query("SELECT * FROM Activity WHERE id = :activityId")
    fun loadLiveActivity(activityId: Long): LiveData<ActivityWithCardioActivity>

    @Insert(onConflict = REPLACE)
    fun insertActivity(activity: Activity): Long

    @Update(onConflict = REPLACE)
    fun updateActivity(activity: Activity)

    @Delete
    fun deleteActivity(activity: Activity)

    @Query("DELETE FROM Activity where id=:activityId")
    fun deleteActivityById(activityId: Long)

}
