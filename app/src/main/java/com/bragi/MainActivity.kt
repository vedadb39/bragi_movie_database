package com.bragi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bragi.core.presentation.navigation.NavigationRoot
import com.bragi.app.ui.theme.BragiMoviesDatabaseTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BragiMoviesDatabaseTheme {
                KoinAndroidContext {
                    NavigationRoot()
                }
            }
        }
    }
}
