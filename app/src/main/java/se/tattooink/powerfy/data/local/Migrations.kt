package se.tattooink.powerfy.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS cart_items (" +
                    "productId INTEGER NOT NULL PRIMARY KEY, " +
                    "quantity INTEGER NOT NULL" +
                    ")"
        )
    }
}