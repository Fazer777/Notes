package com.project.taskplanner.application

import android.app.Application
import com.project.taskplanner.di.appModule
import com.project.taskplanner.di.dataModule
import com.project.taskplanner.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(listOf(
                domainModule,
                appModule,
                dataModule
            ))
        }
    }
}