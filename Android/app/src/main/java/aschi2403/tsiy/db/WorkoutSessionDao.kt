package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy
import aschi2403.tsiy.model.WorkoutSession

@Dao
interface WorkoutSessionDao {

    @Query("SELECT * FROM WorkoutSession")
    fun loadAll(): List<WorkoutSession>

    @Query("SELECT * FROM WorkoutSession WHERE id=:workoutSessionId")
    fun loadWorkoutSession(workoutSessionId: Long): WorkoutSession

    @Query("SELECT * FROM WorkoutSession WHERE id=:workoutSessionId")
    fun loadLiveWorkoutSession(workoutSessionId: Long): LiveData<WorkoutSession>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWorkoutSession(workoutSession: WorkoutSession): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWorkoutSession(workoutSession: WorkoutSession)

    @Delete
    fun deleteWorkoutSession(workoutSession: WorkoutSession)

    @Query("SELECT * FROM WorkoutSession WHERE idForMerging=:idForMerging")
    fun getWorkoutByMergingId(idForMerging: Long): List<WorkoutSession>


    @Query("DELETE FROM WorkoutSession WHERE idForMerging=:idForMerging")
    fun deleteWorkoutSessionByMergingId(idForMerging: Long)

}
