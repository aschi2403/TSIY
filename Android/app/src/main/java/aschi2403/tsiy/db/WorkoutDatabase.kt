package aschi2403.tsiy.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.model.PowerActivityType

@Database(
    entities = arrayOf(PowerActivity::class, PowerActivityType::class, ActivityType::class),
    version = 1
)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun powerActivityDao(): PowerActivityDao
    abstract fun powerActivityTypeDao(): PowerActivityTypeDao
    abstract fun activityTypeDao(): ActivityTypeDao

    companion object {
        private var instance: WorkoutDatabase? = null

        fun getInstance(context: Context): WorkoutDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "Workout"
                ).build()
            }
            return instance as WorkoutDatabase
        }
    }
}