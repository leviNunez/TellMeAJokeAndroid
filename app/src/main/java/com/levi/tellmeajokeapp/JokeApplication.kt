package com.levi.tellmeajokeapp

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.levi.tellmeajokeapp.AppContainer

class JokeApplication : Application() {

    var appContainer: AppContainer = AppContainerImpl()
        @VisibleForTesting set
}