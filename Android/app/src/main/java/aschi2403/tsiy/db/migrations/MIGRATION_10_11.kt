package aschi2403.tsiy.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_10_11: Migration = object : Migration(10, 11) {
    override fun migrate(database: SupportSQLiteDatabase) {
        var tableName = "WorkoutEntry"
        database.execSQL("ALTER TABLE `${tableName}` ADD COLUMN repetitions INTEGER NOT NULL DEFAULT -1")
    }
}