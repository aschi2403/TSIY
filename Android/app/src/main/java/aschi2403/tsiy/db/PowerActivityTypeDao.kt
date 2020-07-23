package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import aschi2403.tsiy.model.PowerActivityType

@Dao
interface PowerActivityTypeDao {
    @Query("SELECT * FROM PowerActivityType")
    fun loadAll(): LiveData<List<PowerActivityType>>

    @Query("SELECT * FROM PowerActivityType WHERE id=:powerActivityTypeId")
    fun loadPowerActivityType(powerActivityTypeId: Long): PowerActivityType

    @Query("SELECT * FROM PowerActivityType WHERE id=:powerActivityTypeId")
    fun loadLivePowerActivityType(powerActivityTypeId: Long): LiveData<PowerActivityType>

    @Insert(onConflict = IGNORE)
    fun insertPowerActivityType(powerActivityType: PowerActivityType): Long

    @Update(onConflict = REPLACE)
    fun updatePowerActivityType(powerActivityType: PowerActivityType)

    @Delete
    fun deletePowerActivityType(powerActivityType: PowerActivityType)
}