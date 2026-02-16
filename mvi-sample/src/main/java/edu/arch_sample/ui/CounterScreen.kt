package edu.arch_sample.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.arch_sample.ui.theme.Typography

@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    uiState: CounterUiState = CounterUiState(),
    onStart: () -> Unit = {},
    onStop: () -> Unit = {},
    onReset: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onStart) {
                Text(
                    text = "START"
                )
            }
            Button(onStop) {
                Text(
                    text = "STOP"
                )
            }
            Button(onReset) {
                Text(
                    text = "RESET"
                )
            }
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = uiState.countText,
                style = Typography.bodyLarge
            )
            Text(
                text = uiState.progressText,
                style = Typography.bodyLarge
            )
        }
    }
}

@Preview()
@Composable
private fun CounterScreenPreview() {
    CounterScreen()
}