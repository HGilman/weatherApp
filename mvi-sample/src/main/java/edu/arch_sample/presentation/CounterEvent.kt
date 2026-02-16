package edu.arch_sample.presentation

sealed interface CounterEvent {

    object StartClicked : CounterEvent

    object StopClicked : CounterEvent

    object ResetClicked : CounterEvent
}