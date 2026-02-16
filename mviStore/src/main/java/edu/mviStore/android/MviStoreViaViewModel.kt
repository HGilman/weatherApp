package edu.mviStore.android

import androidx.lifecycle.ViewModelStoreOwner
import edu.mviStore.core.MviStore
import edu.mviStore.android.utils.generateViewModelKey
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class MviStoreViaViewModel<T : MviStore<*, *, *>>(
    private val viewModelKey: String? = null,
    private val viewModelStoreOwnerProvider: (() -> ViewModelStoreOwner)? = null,
    private val factory: () -> T,
) : ReadOnlyProperty<ViewModelStoreOwner, T> {
    private var value: T? = null

    override fun getValue(thisRef: ViewModelStoreOwner, property: KProperty<*>): T {
        return value ?: run {
            val key = viewModelKey ?: generateViewModelKey(thisRef, property)
            val viewModelStore = (viewModelStoreOwnerProvider?.invoke() ?: thisRef).viewModelStore
            val vm = viewModelStore.get(key) { MviStoreViewModel(factory()) }
            vm.store.also { value = it }
        }
    }
}
