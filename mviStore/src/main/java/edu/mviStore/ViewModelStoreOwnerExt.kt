package edu.mviStore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import edu.mviStore.android.MviStoreViaViewModel
import edu.mviStore.core.MviStore
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty

/**
 * Lazily gets kotea [Store] from the [ViewModelStore].
 * Launches it in the [viewModelScope][ViewModel.viewModelScope] with the specified [CoroutineContext] on first access.
 *
 * By default uses a containing class and property names as a [ViewModel] key,
 * but you can share [Store] across a screens by specifying the [sharedViewModelKey].
 *
 * @param coroutineContext [CoroutineContext] to launch [Store] in.
 * @param sharedViewModelKey key to share [Store] across a screens.
 * @param factory factory to create [Store]
 */
fun <T : MviStore<*, *, *>> mviStoreViaViewModel(
    sharedViewModelKey: String? = null,
    factory: () -> T
): ReadOnlyProperty<ViewModelStoreOwner, T> {
    return MviStoreViaViewModel(
        viewModelKey = sharedViewModelKey,
        factory = factory,
    )
}

/**
 * Lazily gets kotea [Store] from the [ViewModelStore].
 * Launches it in the [viewModelScope][ViewModel.viewModelScope] with the specified [CoroutineContext] on first access.
 *
 * By default uses a containing class and property names as a [ViewModel] key,
 * but you can share [Store] across a screens by specifying the [sharedViewModelKey].
 *
 * @param coroutineContext [CoroutineContext] to launch [Store] in.
 * @param sharedViewModelKey key to share [Store] across a screens.
 * @param sharedViewModelStoreOwner that [Store] will associate with.
 * @param factory factory to create [Store]
 */
fun <T : MviStore<*, *, *>> mviStoreViaViewModel(
    sharedViewModelKey: String,
    sharedViewModelStoreOwner: () -> ViewModelStoreOwner,
    factory: () -> T,
): ReadOnlyProperty<ViewModelStoreOwner, T> {
    return MviStoreViaViewModel(
        viewModelKey = sharedViewModelKey,
        viewModelStoreOwnerProvider = sharedViewModelStoreOwner,
        factory = factory,
    )
}