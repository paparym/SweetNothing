package com.prestigerito.sweetnothing.ui.rules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prestigerito.sweetnothing.MR
import com.prestigerito.sweetnothing.ui.menu.EndlessBackground

@Composable
fun RulesScreen(
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        EndlessBackground(
            asset = MR.images.symbol_bg,
            topToBottom = false,
        )
        Text("rules screen")
        Icon(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(16.dp)
                .align(Alignment.TopStart)
                .clickable { onBack.invoke() },
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
        )
    }
}
