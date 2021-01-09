package aschi2403.tsiy.db

import androidx.room.*
import aschi2403.tsiy.model.SetEntry

@Dao
interface SetEntryDao {
    @Query("SELECT * FROM SetEntry WHERE id = :powerActivityId")
    fun getSetEntriesByPowerActivityId(powerActivityId: Long): List<SetEntry>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSetEntry(setEntry: SetEntry): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSetEntry(setEntry: SetEntry)

    @Delete
    fun deleteSetEntry(setEntry: SetEntry)
}