package com.perryjackson.walletqr.ui.home

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.perryjackson.walletqr.data.history.ScanHistoryEntity
import com.perryjackson.walletqr.data.history.ScanHistoryRepository
import com.perryjackson.walletqr.ui.generate.QrCodeGenerator
import com.perryjackson.walletqr.ui.scan.GoogleCodeScannerLauncher
import java.text.DateFormat
import java.util.Date
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var scannedValue by remember { mutableStateOf<String?>(null) }
    var scanError by remember { mutableStateOf<String?>(null) }
    var qrText by remember { mutableStateOf("") }
    var generatedQrCode by remember { mutableStateOf<Bitmap?>(null) }
    var generationError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val applicationContext = context.applicationContext
    val scannerLauncher = remember(context) {
        GoogleCodeScannerLauncher(context)
    }
    val scanHistoryRepository = remember(applicationContext) {
        ScanHistoryRepository(applicationContext)
    }
    val scanHistory by scanHistoryRepository.history.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WalletQR") }
            )
        }
    ) { contentPadding ->
        HomeContent(
            contentPadding = contentPadding,
            scannedValue = scannedValue,
            scanError = scanError,
            qrText = qrText,
            generatedQrCode = generatedQrCode,
            generationError = generationError,
            scanHistory = scanHistory,
            onScanCode = {
                scannerLauncher.startScan(
                    onScanned = { rawValue ->
                        scannedValue = rawValue
                        scanError = null
                        coroutineScope.launch {
                            scanHistoryRepository.recordSuccessfulScan(rawValue)
                        }
                    },
                    onFailure = {
                        scanError = "Unable to scan code."
                    }
                )
            },
            onQrTextChanged = { text ->
                qrText = text
                generatedQrCode = null
                generationError = null
            },
            onGenerateQrCode = {
                val qrCode = QrCodeGenerator.generate(qrText)
                generatedQrCode = qrCode
                generationError = if (qrCode == null) {
                    "Unable to generate QR code."
                } else {
                    null
                }
            }
        )
    }
}

@Composable
private fun HomeContent(
    contentPadding: PaddingValues,
    scannedValue: String?,
    scanError: String?,
    qrText: String,
    generatedQrCode: Bitmap?,
    generationError: String?,
    scanHistory: List<ScanHistoryEntity>,
    onScanCode: () -> Unit,
    onQrTextChanged: (String) -> Unit,
    onGenerateQrCode: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("A local QR and barcode utility.")
        Button(
            onClick = onScanCode,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Scan code")
        }
        scannedValue?.let { rawValue ->
            Text(
                text = rawValue,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        scanError?.let { errorMessage ->
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        OutlinedTextField(
            value = qrText,
            onValueChange = onQrTextChanged,
            label = { Text("Text") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )
        Button(
            onClick = onGenerateQrCode,
            enabled = qrText.isNotBlank(),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Generate")
        }
        generatedQrCode?.let { qrCode ->
            Image(
                bitmap = qrCode.asImageBitmap(),
                contentDescription = "Generated QR code",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(256.dp)
            )
        }
        generationError?.let { errorMessage ->
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        if (scanHistory.isNotEmpty()) {
            Text(
                text = "History",
                modifier = Modifier.padding(top = 24.dp)
            )
            scanHistory.forEach { entry ->
                Text(
                    text = entry.rawValue,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = DateFormat.getDateTimeInstance().format(
                        Date(entry.scannedAtEpochMillis)
                    )
                )
            }
        }
    }
}
