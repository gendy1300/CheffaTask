package com.agendy.chefaa.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.Navigation.findNavController
import com.agendy.chefaa.R
import com.agendy.chefaa.main.MainScreen
import com.agendy.chefaa.utils.theme.ChefaaTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        installSplashScreen()


        enableEdgeToEdge()

        setContent {
            ChefaaTheme {
                MainScreen()
            }

        }
    }


}




