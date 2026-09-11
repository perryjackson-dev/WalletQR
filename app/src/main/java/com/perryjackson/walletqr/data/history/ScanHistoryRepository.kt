package com.perryjackson.walletqr.data.history

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow

class ScanHistoryRepository(context: Context) {
    private val database = Room.databaseBuilder(
        context.applicationContext,
        WalletQrDatabase::class.java,
        DATABASE_NAME
    ).build()
    private val scanHistoryDao = database.scanHistoryDao()

    val history: Flow<List<ScanHistoryEntity>> = scanHistoryDao.observeAll()

    suspend fun recordSuccessfulScan(rawValue: String) {
        scanHistoryDao.insert(
            ScanHistoryEntity(
                rawValue = rawValue,
                scannedAtEpochMillis = System.currentTimeMillis()
            )
        )
    }

    private companion object {
        const val DATABASE_NAME = "walletqr-history.db"
    }
}
