package com.agendy.chefaa.activities

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.agendy.chefaa.main.MainScreen
import com.agendy.chefaa.utils.theme.ChefaaTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        installSplashScreen()


        enableEdgeToEdge()


        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { _: Boolean ->
            }


        requestPermissionLauncher.launch(
            Manifest.permission.POST_NOTIFICATIONS
        )





        setContent {
            ChefaaTheme {
                MainScreen()
            }

        }
    }


}




