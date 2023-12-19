package com.prestigerito.sweetnothing.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import com.prestigerito.sweetnothing.AppScreen
import com.prestigerito.sweetnothing.navigation.RootComponent

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = retainedComponent {
            RootComponent(it)
        }
        setContent {
            AppScreen(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = true,
                root = root,
            )
        }
    }
}
