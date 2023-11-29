package com.prestigerito.sweetnothing

import com.prestigerito.sweetnothing.ui.menu.AnimationType

actual fun platformDependentAnimationType(animationType: AnimationType): AnimationType {
    return if (animationType == AnimationType.ASSET_CHANGE) AnimationType.Y_AXIS_ROTATION else animationType
}
