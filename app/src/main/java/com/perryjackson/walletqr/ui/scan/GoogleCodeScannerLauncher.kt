package com.perryjackson.walletqr.ui.scan

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class GoogleCodeScannerLauncher(
    private val context: Context
) {
    fun startScan(
        onScanned: (String) -> Unit,
        onFailure: () -> Unit
    ) {
        val activity = context.findActivity()
        if (activity == null) {
            onFailure()
            return
        }

        GmsBarcodeScanning.getClient(activity)
            .startScan()
            .addOnSuccessListener { barcode ->
                barcode.rawValue?.let(onScanned) ?: onFailure()
            }
            .addOnFailureListener {
                onFailure()
            }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
