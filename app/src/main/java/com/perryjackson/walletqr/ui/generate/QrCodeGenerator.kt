package com.perryjackson.walletqr.ui.generate

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

object QrCodeGenerator {
    private const val IMAGE_SIZE = 512

    fun generate(text: String): Bitmap? = try {
        val matrix = QRCodeWriter().encode(
            text,
            BarcodeFormat.QR_CODE,
            IMAGE_SIZE,
            IMAGE_SIZE,
            mapOf(EncodeHintType.CHARACTER_SET to "UTF-8")
        )
        val pixels = IntArray(IMAGE_SIZE * IMAGE_SIZE) { index ->
            val x = index % IMAGE_SIZE
            val y = index / IMAGE_SIZE
            if (matrix[x, y]) Color.BLACK else Color.WHITE
        }

        Bitmap.createBitmap(pixels, IMAGE_SIZE, IMAGE_SIZE, Bitmap.Config.ARGB_8888)
    } catch (_: WriterException) {
        null
    }
}
