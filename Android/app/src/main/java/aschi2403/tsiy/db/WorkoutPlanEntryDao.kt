package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy
import aschi2403.tsiy.model.WorkoutPlanEntry

@Dao
interface WorkoutPlanEntryDao {

    @Query("SELECT * FROM WorkoutPlanEntry")
    fun loadAll(): List<WorkoutPlanEntry>

    @Query("SELECT * FROM WorkoutPlanEntry WHERE id=:workoutEntryId")
    fun loadWorkoutEntry(workoutEntryId: Long): WorkoutPlanEntry

    @Query("SELECT * FROM WorkoutPlanEntry WHERE id=:workoutEntryId")
    fun loadLiveWorkoutEntry(workoutEntryId: Long): LiveData<WorkoutPlanEntry>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWorkoutEntry(workoutEntry: WorkoutPlanEntry): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWorkoutEntry(workoutEntry: WorkoutPlanEntry)

    @Delete
    fun deleteWorkoutEntry(workoutEntry: WorkoutPlanEntry)
}
