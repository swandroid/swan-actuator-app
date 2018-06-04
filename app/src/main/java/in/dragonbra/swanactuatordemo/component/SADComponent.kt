package `in`.dragonbra.swanactuatordemo.component

import `in`.dragonbra.swanactuatordemo.module.AppModule
import `in`.dragonbra.swanactuatordemo.module.ViewModelModule
import `in`.dragonbra.swanactuatordemo.ui.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface SADComponent {
    fun inject(mainFragment: MainFragment)
}