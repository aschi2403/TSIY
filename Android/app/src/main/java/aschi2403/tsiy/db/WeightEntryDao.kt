package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import aschi2403.tsiy.model.WeightEntry

@Dao
interface WeightEntryDao {
    @Query("SELECT * FROM WeightEntry")
    fun loadAll(): List<WeightEntry>

    @Query("SELECT * FROM WeightEntry WHERE id=:weightEntryId")
    fun loadWeightEntry(weightEntryId: Long): WeightEntry

    @Query("SELECT * FROM WeightEntry WHERE id=:weightEntryId")
    fun loadLiveWeightEntry(weightEntryId: Long): LiveData<WeightEntry>

    @Insert(onConflict = IGNORE)
    fun insertWeightEntry(weightEntry: WeightEntry): Long

    @Update(onConflict = REPLACE)
    fun updateWeightEntry(weightEntry: WeightEntry)

    @Delete
    fun deleteWeightEntry(weightEntry: WeightEntry)
}