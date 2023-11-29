package com.prestigerito.sweetnothing.ui.rules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.sp
import com.prestigerito.sweetnothing.MR
import com.prestigerito.sweetnothing.ui.RuleButton
import com.prestigerito.sweetnothing.ui.game.coinAssets
import com.prestigerito.sweetnothing.ui.game.enemy1Asset
import com.prestigerito.sweetnothing.ui.game.enemy2Asset
import com.prestigerito.sweetnothing.ui.menu.AnimatedItem
import com.prestigerito.sweetnothing.ui.menu.AnimationType
import com.prestigerito.sweetnothing.ui.menu.EndlessBackground
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun RulesScreen(
    onBack: () -> Unit,
    onStartGame: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        EndlessBackground(
            asset = MR.images.symbol_bg,
            topToBottom = false,
        )
        Icon(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(16.dp)
                .align(Alignment.TopStart)
                .clickable { onBack.invoke() },
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
        )
        RuleButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            text = stringResource(MR.strings.letsPlay),
            onClick = { onStartGame.invoke() },
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = stringResource(MR.strings.rules),
                fontSize = 30.sp,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(MR.strings.grab),
                    fontSize = 20.sp,
                )
                AnimatedItem(
                    modifier = Modifier.size(100.dp),
                    assets = coinAssets,
                    animationType = AnimationType.Y_AXIS_ROTATION,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(MR.strings.avoid),
                    fontSize = 20.sp,
                )
                AnimatedItem(
                    modifier = Modifier.size(100.dp),
                    assets = enemy1Asset,
                    animationType = AnimationType.Y_AXIS_ROTATION,
                )
                AnimatedItem(
                    modifier = Modifier.size(100.dp),
                    assets = enemy2Asset,
                    animationType = AnimationType.Y_AXIS_ROTATION,
                )
            }
        }
    }
}
