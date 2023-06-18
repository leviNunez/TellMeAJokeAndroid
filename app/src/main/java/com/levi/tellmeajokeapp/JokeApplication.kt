package com.levi.tellmeajokeapp

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.levi.tellmeajokeapp.model.AppContainer
import com.levi.tellmeajokeapp.model.DefaultAppContainer

class JokeApplication : Application() {

    var appContainer: AppContainer = DefaultAppContainer()
        @VisibleForTesting set
}