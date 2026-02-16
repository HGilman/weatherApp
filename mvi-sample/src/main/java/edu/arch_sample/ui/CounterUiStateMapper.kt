package edu.arch_sample.ui

import edu.arch_sample.presentation.CounterState

fun mapState(state: CounterState): CounterUiState {
    val progressText = if (state.isProgress) {
        "Started"
    } else {
        "Stopped"
    }
    val countText = "Count: ${state.count}"

    return CounterUiState(
        countText = countText,
        progressText = progressText
    )
}