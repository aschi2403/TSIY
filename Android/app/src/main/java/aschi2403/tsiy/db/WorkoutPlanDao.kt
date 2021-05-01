package aschi2403.tsiy.db

import androidx.room.*
import aschi2403.tsiy.model.WorkoutPlan

@Dao
interface WorkoutPlanDao {
    @Query("SELECT * FROM WorkoutPlan")
    fun loadAll(): List<WorkoutPlan>

    @Query("SELECT * FROM WorkoutPlan WHERE id=:workoutEntryId")
    fun loadWorkoutPlan(workoutEntryId: Long): WorkoutPlan

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWorkoutPlan(workoutPlan: WorkoutPlan): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWorkoutPlan(workoutPlan: WorkoutPlan)

    @Delete
    fun deleteWorkoutPlan(workoutPlan: WorkoutPlan)
}