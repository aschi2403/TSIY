package aschi2403.tsiy.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import aschi2403.tsiy.db.migrations.MIGRATION_8_9
import aschi2403.tsiy.db.migrations.MIGRATION_9_10
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.model.WeightEntry
import aschi2403.tsiy.model.SetEntry
import aschi2403.tsiy.model.WorkoutEntry
import aschi2403.tsiy.model.WorkoutPlan
import aschi2403.tsiy.model.GPSPoint

@Database(
    entities = [
        PowerActivity::class,
        ActivityType::class,
        GeneralActivity::class,
        WeightEntry::class,
        SetEntry::class,
        WorkoutEntry::class,
        WorkoutPlan::class,
        GPSPoint::class],
    version = 10,
    exportSchema = true
)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun powerActivityDao(): PowerActivityDao
    abstract fun powerActivityTypeDao(): PowerActivityTypeDao
    abstract fun activityTypeDao(): ActivityTypeDao
    abstract fun allActivityTypeDao(): AllActivityTypeDao
    abstract fun generalActivityDao(): GeneralActivityDao
    abstract fun weightEntryDao(): WeightEntryDao
    abstract fun setEntryDao(): SetEntryDao
    abstract fun workoutPlanDao(): WorkoutPlanDao
    abstract fun workoutEntryDao(): WorkoutEntryDao
    abstract fun gpsPointDao(): GPSPointsDao

    companion object {
        private var instance: WorkoutDatabase? = null

        fun getInstance(context: Context): WorkoutDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "Workout.db"
                ).addMigrations(MIGRATION_8_9).addMigrations(MIGRATION_9_10)
                    .createFromAsset("database/Workout_template.db")
                    .allowMainThreadQueries().build()
            }
            return instance as WorkoutDatabase
        }
    }
}
