package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy
import aschi2403.tsiy.model.GPSPoint

@Dao
interface GPSPointsDao {
    @Query("SELECT * FROM GPSPoints")
    fun loadAll(): List<GPSPoint>

    @Query("SELECT * FROM GPSPoints WHERE workoutEntryId=:workoutEntryId")
    fun loadGPSPoints(workoutEntryId: Long): List<GPSPoint>

    @Query("SELECT * FROM GPSPoints WHERE id=:workoutEntryId")
    fun loadLiveGPSPoints(workoutEntryId: Long): LiveData<GPSPoint>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGPSPoint(gPSPoint: GPSPoint): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateGPSPoint(gPSPoint: GPSPoint)

    @Delete
    fun deleteGPSPoint(gPSPoint: GPSPoint)
}
