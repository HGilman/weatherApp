package edu.arch_sample.presentation

sealed interface CounterEffect {

    data class ShowToast(
        val text: String,
    ) : CounterEffect
}