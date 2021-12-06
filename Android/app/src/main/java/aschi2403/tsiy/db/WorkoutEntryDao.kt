package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy
import aschi2403.tsiy.model.WorkoutEntry

@Dao
interface WorkoutEntryDao {

    @Query("SELECT * FROM WorkoutEntry")
    fun loadAll(): List<WorkoutEntry>

    @Query("SELECT * FROM WorkoutEntry WHERE id=:workoutEntryId")
    fun loadWorkoutEntry(workoutEntryId: Long): WorkoutEntry

    @Query("SELECT * FROM WorkoutEntry WHERE id=:workoutEntryId")
    fun loadLiveWorkoutEntry(workoutEntryId: Long): LiveData<WorkoutEntry>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWorkoutEntry(workoutEntry: WorkoutEntry): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWorkoutEntry(workoutEntry: WorkoutEntry)

    @Delete
    fun deleteWorkoutEntry(workoutEntry: WorkoutEntry)
}
