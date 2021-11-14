package aschi2403.tsiy.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import aschi2403.tsiy.model.SetEntry

@Dao
interface SetEntryDao {
    @Query("SELECT * FROM SetEntry WHERE powerActivityId = :powerActivityId")
    fun getSetEntriesByPowerActivityId(powerActivityId: Long): List<SetEntry>

    @Query("SELECT * FROM SetEntry")
    fun getAllSets(): List<SetEntry>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSetEntry(setEntry: SetEntry): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSetEntry(setEntry: SetEntry)

    @Delete
    fun deleteSetEntry(setEntry: SetEntry)
}
