package com.perryjackson.walletqr.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.perryjackson.walletqr.ui.scan.GoogleCodeScannerLauncher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var scannedValue by remember { mutableStateOf<String?>(null) }
    var scanError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scannerLauncher = remember(context) {
        GoogleCodeScannerLauncher(context)
    }

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
            onScanCode = {
                scannerLauncher.startScan(
                    onScanned = { rawValue ->
                        scannedValue = rawValue
                        scanError = null
                    },
                    onFailure = {
                        scanError = "Unable to scan code."
                    }
                )
            }
        )
    }
}

@Composable
private fun HomeContent(
    contentPadding: PaddingValues,
    scannedValue: String?,
    scanError: String?,
    onScanCode: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 24.dp, vertical = 16.dp)
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
    }
}
