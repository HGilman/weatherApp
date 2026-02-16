package edu.mviStore.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MviStore<State, Event, Effect> {

    val stateFlow: StateFlow<State>

    val effects: Flow<Effect>

    fun dispatch(event: Event)

    fun initScope(coroutineScope: CoroutineScope)
}