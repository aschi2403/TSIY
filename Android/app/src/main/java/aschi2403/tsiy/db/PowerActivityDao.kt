package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import aschi2403.tsiy.model.PowerActivity

@Dao
interface PowerActivityDao {
    @Query("SELECT * FROM PowerActivity")
    fun loadAll(): /*LiveData<*/List<PowerActivity>/*>*/

    @Query("SELECT * FROM PowerActivity WHERE id= :powerActivityId")
    fun loadPowerActivity(powerActivityId: Long): PowerActivity

    @Query("SELECT * FROM PowerActivity WHERE id = :powerActivityId")
    fun loadLivePowerActivity(powerActivityId: Long): LiveData<PowerActivity>

    @Insert(onConflict = IGNORE)
    fun insertPowerActivity(powerActivity: PowerActivity): Long

    @Update(onConflict = REPLACE)
    fun updatePowerActivity(powerActivity: PowerActivity)

    @Delete
    fun deletePowerActivity(powerActivity: PowerActivity)

}
