package com.prestigerito.sweetnothing

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.prestigerito.sweetnothing.di.AppModule
import com.prestigerito.sweetnothing.navigation.MainScreenNav
import com.prestigerito.sweetnothing.presentation.MainMenuViewModel
import com.prestigerito.sweetnothing.ui.AppTheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun AppScreen(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    appModule: AppModule,
) {
    AppTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    ) {
        val viewModel = getViewModel(
            key = "main-menu-screen",
            factory = viewModelFactory {
                MainMenuViewModel(scoreDataSource = appModule.scoreDataSource)
            },
        )
        Navigator(MainScreenNav(viewModel = viewModel))
    }
}
