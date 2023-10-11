package com.prestigerito.sweetnothing.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.geometry.Rect

@Composable
internal fun rememberItemFallState(): ToolbarState {
    return rememberSaveable(
        saver = ExitUntilCollapsedState.Saver,
//        inputs = arrayOf(toolbarHeightRange)
    ) {
        ExitUntilCollapsedState()
    }
}

@Stable
interface ToolbarState {
    var coordinates: Rect
}

abstract class ScrollFlagState() : ToolbarState {
    internal abstract var _coordinates: Rect
}

class ExitUntilCollapsedState(
    coord: Rect = Rect.Zero,
) : ScrollFlagState() {
    override var _coordinates by mutableStateOf(
//        value = scrollOffset.coerceIn(0f, rangeDifference.toFloat()),
        value = coord,
        policy = structuralEqualityPolicy(),
    )

    override var coordinates: Rect
        get() = _coordinates
        set(value) {
            _coordinates = value
//                .coerceIn(0f, rangeDifference.toFloat())
        }

    companion object {
        val Saver = run {

            val coordinatesKey = "Coordinates"

            mapSaver(
                save = {
                    mapOf(
                        coordinatesKey to it.coordinates
                    )
                },
                restore = {
                    ExitUntilCollapsedState(
                        coord = (it[coordinatesKey] as Rect)
                    )
                },
            )
        }
    }
}