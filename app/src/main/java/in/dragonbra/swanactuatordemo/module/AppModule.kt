package `in`.dragonbra.swanactuatordemo.module

import `in`.dragonbra.swanactuatordemo.SADApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: SADApp) {

    @Provides
    @Singleton
    fun provideApp(): SADApp = context
}