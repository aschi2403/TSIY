package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.*
import aschi2403.tsiy.model.GPSPoints

@Dao
interface GPSPointsDao {
    @Query("SELECT * FROM GPSPoints")
    fun loadAll(): List<GPSPoints>

    @Query("SELECT * FROM GPSPoints WHERE id=:workoutEntryId")
    fun loadGPSPoints(workoutEntryId: Long): GPSPoints

    @Query("SELECT * FROM GPSPoints WHERE id=:workoutEntryId")
    fun loadLiveGPSPoints(workoutEntryId: Long): LiveData<GPSPoints>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGPSPoint(gPSPoints: GPSPoints): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateGPSPoint(gPSPoints: GPSPoints)

    @Delete
    fun deleteGPSPoint(gPSPoints: GPSPoints)
}