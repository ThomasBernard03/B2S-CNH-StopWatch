package fr.b2s.cnhstopwatch

import android.app.Application
import fr.b2s.cnhstopwatch.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CNHApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CNHApplication)
            modules(appModule)
        }
    }
}
