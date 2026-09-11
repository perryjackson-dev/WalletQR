package com.perryjackson.walletqr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.perryjackson.walletqr.ui.home.HomeScreen
import com.perryjackson.walletqr.ui.theme.WalletQRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalletQRTheme {
                HomeScreen()
            }
        }
    }
}
