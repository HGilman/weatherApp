package edu.arch_sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

inline fun <reified T> Flow<T>.collectOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.RESUMED,
    collector: FlowCollector<T>
) {
    lifecycleOwner.lifecycleScope.launch {
        this@collectOnLifecycle
            .flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
            .collect(collector)
    }
}

@Composable
fun <T> CollectOnLifecycle(
    flow: Flow<T>,
    minActiveState: Lifecycle.State = Lifecycle.State.RESUMED,
    collector: FlowCollector<T>
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            flow.flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState)
                .collect(collector)
        }
    }
}
