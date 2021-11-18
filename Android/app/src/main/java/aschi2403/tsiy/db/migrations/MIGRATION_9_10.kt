package aschi2403.tsiy.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_9_10: Migration = object : Migration(9, 10) {
    override fun migrate(database: SupportSQLiteDatabase) {
        var tableName = "PowerActivity"
        database.execSQL("ALTER TABLE `${tableName}` ADD COLUMN workoutPlanId INTEGER NOT NULL DEFAULT -1")
        database.execSQL("ALTER TABLE `${tableName}` ADD COLUMN workoutId INTEGER NOT NULL DEFAULT -1")


        tableName = "GeneralActivity"
        database.execSQL("ALTER TABLE `${tableName}` ADD COLUMN workoutPlanId INTEGER NOT NULL DEFAULT -1")
        database.execSQL("ALTER TABLE `${tableName}` ADD COLUMN workoutId INTEGER NOT NULL DEFAULT -1")
    }
}
