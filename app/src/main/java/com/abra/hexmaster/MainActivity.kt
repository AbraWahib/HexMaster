package com.abra.hexmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.abra.hexmaster.ui.navigation.HexMasterNavGraph
import com.abra.hexmaster.ui.theme.HexMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val appContainer = (application as HexMasterApplication).container
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            HexMasterTheme {
                val navController = rememberNavController()
                    HexMasterNavGraph(
                        navController = navController,
                        appContainer = appContainer,
                    )
                }

        }
    }
}
