package com.prestigerito.sweetnothing.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.prestigerito.sweetnothing.AppScreen
import com.prestigerito.sweetnothing.di.AppModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = true,
                appModule = AppModule(context = LocalContext.current.applicationContext),
            )
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    AppScreen(
        darkTheme = isSystemInDarkTheme(),
        dynamicColor = true,
        appModule = AppModule(context = LocalContext.current.applicationContext),
    )
}
