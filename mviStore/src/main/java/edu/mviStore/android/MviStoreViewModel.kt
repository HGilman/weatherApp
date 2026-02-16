package edu.mviStore.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mviStore.core.MviStore

class MviStoreViewModel <S : MviStore<*, *, *>> (
    val store: S
) : ViewModel() {

    init {
        store.initScope(viewModelScope)
    }
}