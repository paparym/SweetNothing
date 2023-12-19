package com.prestigerito.sweetnothing.presentation.rules

import com.arkivanov.decompose.ComponentContext

class RulesComponent(
    componentContext: ComponentContext,
    private val onStartGame: () -> Unit,
    private val onBack: () -> Unit,
) : ComponentContext by componentContext {
    fun onStartGame() = onStartGame.invoke()
    fun onBack() = onBack.invoke()
}
