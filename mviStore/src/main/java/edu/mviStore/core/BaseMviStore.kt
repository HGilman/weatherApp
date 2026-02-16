package edu.mviStore.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseMviStore<State, Event, Effect> (
    initialState: State,
) : MviStore<State, Event, Effect> {

    protected lateinit var scope: CoroutineScope

    private val _state = MutableStateFlow(initialState)
    override val stateFlow = _state.asStateFlow()

    private val _effects = Channel<Effect>(capacity = Channel.Factory.UNLIMITED)
    override val effects: Flow<Effect> = _effects.receiveAsFlow()

    protected val state: State
        get() = _state.value

    protected fun updateState(block: State.() -> State) {
        _state.update { _state.value.block() }
    }

    protected fun emitEffect(effect: Effect) {
        scope.launch {
            _effects.send(effect)
        }
    }

    override fun initScope(coroutineScope: CoroutineScope) {
        scope = coroutineScope
    }
}