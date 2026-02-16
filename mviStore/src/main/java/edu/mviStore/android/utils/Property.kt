package edu.mviStore.android.utils

import androidx.lifecycle.ViewModelStoreOwner
import kotlin.reflect.KProperty

internal fun generateViewModelKey(thisRef: ViewModelStoreOwner, property: KProperty<*>): String {
    return thisRef::class.java.canonicalName!! + "#" + property.name
}