package edu.arch_sample.presentation

import edu.mviStore.core.BaseMviStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private const val MAX_TICK_COUNT = 5

class CounterStore(
    private val maxTickCount: Int = MAX_TICK_COUNT
) : BaseMviStore<CounterState, CounterEvent, CounterEffect>(
    initialState = CounterState(0, false)
) {
    private var tickJob: Job? = null

    private fun tickFlow(): Flow<Unit> = flow {
        while (true) {
            delay(1000L)
            emit(Unit)
        }
    }

    override fun dispatch(event: CounterEvent) {
        when (event) {
            CounterEvent.ResetClicked -> {
                onReset()
            }

            CounterEvent.StartClicked -> {
                onStart()
            }

            CounterEvent.StopClicked -> {
                onStop()
            }
        }
    }

    private fun onReset() {
        val currentCount = stateFlow.value.count
        updateState { copy(count = 0) }
        sendToastEvent("Counter reset, count was $currentCount")
    }

    private fun onStart() {
        if (state.isProgress) {
            sendToastEvent("Counter already started")
        } else {
            sendToastEvent("Counter started")
            updateState { copy(isProgress = true) }
            tickJob = scope.launch {
                tickFlow().collect { it: Unit ->
                    val newCount = state.count + 1
                    if (newCount > maxTickCount) {
                        updateState { copy(isProgress = false) }
                        sendToastEvent("Counter reached maximum")
                        cancel()
                    } else {
                        updateState { copy(count = count + 1) }
                    }
                }
            }
        }
    }

    private fun onStop() {
        updateState { copy(isProgress = false) }
        sendToastEvent("Counter stopped")
        tickJob?.cancel()
    }

    private fun sendToastEvent(message: String) {
        emitEffect(CounterEffect.ShowToast(message))
    }
}