package aschi2403.tsiy.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import aschi2403.tsiy.model.*

@Database(
    entities = [
        PowerActivity::class,
        ActivityType::class,
        GeneralActivity::class,
        WeightEntry::class,
        SetEntry::class,
        WorkoutEntry::class,
        WorkoutPlan::class],
    version = 7
)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun powerActivityDao(): PowerActivityDao
    abstract fun powerActivityTypeDao(): PowerActivityTypeDao
    abstract fun activityTypeDao(): ActivityTypeDao
    abstract fun generalActivityDao(): GeneralActivityDao
    abstract fun weightEntryDao(): WeightEntryDao
    abstract fun setEntryDao(): SetEntryDao
    abstract fun workoutPlanDao(): WorkoutPlanDao
    abstract fun workoutEntryDao(): WorkoutEntryDao

    companion object {
        private var instance: WorkoutDatabase? = null

        fun getInstance(context: Context): WorkoutDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "Workout.db"
                ).allowMainThreadQueries()/*.createFromAsset("database/Workout_temp.db")*/.build()
            }
            return instance as WorkoutDatabase
        }
    }
}