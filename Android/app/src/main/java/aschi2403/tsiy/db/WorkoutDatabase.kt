package aschi2403.tsiy.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import aschi2403.tsiy.model.*

@Database(
    entities = arrayOf(
        PowerActivity::class,
        PowerActivityType::class,
        ActivityType::class,
        GeneralActivity::class,
        WeightEntry::class
    ),
    version = 2
)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun powerActivityDao(): PowerActivityDao
    abstract fun powerActivityTypeDao(): PowerActivityTypeDao
    abstract fun activityTypeDao(): ActivityTypeDao
    abstract fun generalActivityDao(): GeneralActivityDao
    abstract fun weightEntryDao(): WeightEntryDao

    companion object {
        private var instance: WorkoutDatabase? = null

        fun getInstance(context: Context): WorkoutDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "Workout.db"
                )/*.createFromAsset("database/Workout_temp.db")*/.build()
            }
            return instance as WorkoutDatabase
        }
    }
}