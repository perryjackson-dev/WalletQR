package com.perryjackson.walletqr.data.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {
    @Insert
    suspend fun insert(entry: ScanHistoryEntity)

    @Query("SELECT * FROM scan_history ORDER BY scannedAtEpochMillis DESC")
    fun observeAll(): Flow<List<ScanHistoryEntity>>
}
