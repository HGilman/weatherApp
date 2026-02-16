package edu.arch_sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.arch_sample.presentation.CounterEffect
import edu.arch_sample.presentation.CounterEvent
import edu.arch_sample.presentation.CounterStore
import edu.arch_sample.ui.CounterScreen
import edu.arch_sample.ui.mapState
import edu.arch_sample.ui.theme.WeatherAppTheme
import edu.mviStore.mviStoreViaViewModel
import javax.inject.Inject
import javax.inject.Provider

class CounterActivity : ComponentActivity() {

    @Inject
    lateinit var storeProvider: Provider<CounterStore>
    val store: CounterStore by mviStoreViaViewModel {
        storeProvider.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as App).appComponent.inject(this)

        enableEdgeToEdge()
        setContent {
            val state by store.stateFlow.collectAsStateWithLifecycle()
            val context = LocalContext.current

            CollectOnLifecycle(store.effects) { effect ->
                when (effect) {
                    is CounterEffect.ShowToast -> {
                        Toast.makeText(context, effect.text, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CounterScreen(
                        modifier = Modifier.padding(innerPadding),
                        uiState = mapState(state),
                        onStart = { store.dispatch(CounterEvent.StartClicked) },
                        onStop = { store.dispatch(CounterEvent.StopClicked) },
                        onReset = { store.dispatch(CounterEvent.ResetClicked) }
                    )
                }
            }
        }
    }
}