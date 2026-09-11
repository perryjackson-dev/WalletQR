package com.perryjackson.walletqr.data.history

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ScanHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WalletQrDatabase : RoomDatabase() {
    abstract fun scanHistoryDao(): ScanHistoryDao
}
