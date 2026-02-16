package edu.arch_sample.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import edu.arch_sample.CounterActivity
import edu.arch_sample.presentation.CounterStore
import javax.inject.Named

@Component(
    modules = [
        CounterModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun inject(activity: CounterActivity)
}

@Module
object CounterModule {

    @Named("tickCount")
    @Provides
    fun provideMaxTickCount(): Int = 10

    @Provides
    fun provideCounterStore(
        @Named("tickCount") tickCount: Int
    ) : CounterStore {
        return CounterStore(tickCount)
    }
}

