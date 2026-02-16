package edu.arch_sample

import android.app.Application
import edu.arch_sample.di.AppComponent
import edu.arch_sample.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}