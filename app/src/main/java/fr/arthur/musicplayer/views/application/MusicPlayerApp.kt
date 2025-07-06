package fr.arthur.musicplayer.views.application

import android.app.Application
import fr.arthur.musicplayer.helpers.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MusicPlayerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MusicPlayerApp)
            modules(appModule)
        }
    }
}