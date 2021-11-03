package aschi2403.tsiy.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_8_9: Migration = object : Migration(8, 9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val tableName = "GPSPoints"
        database.execSQL("CREATE TABLE IF NOT EXISTS `${tableName}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `workoutEntryId` INTEGER NOT NULL)")

        database.execSQL("CREATE INDEX IF NOT EXISTS `index_GPSPoints_workoutEntryId` ON `${tableName}` (`workoutEntryId`)")
    }
}