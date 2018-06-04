package `in`.dragonbra.swanactuatordemo.module

import `in`.dragonbra.swanactuatordemo.ui.main.MainViewModel
import `in`.dragonbra.swanactuatordemo.viewmodel.SADViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun provideSADViewModelProvider(mainViewModel: MainViewModel) =
            SADViewModelProvider(mainViewModel)

    @Provides
    @Singleton
    fun provideMainViewModel() = MainViewModel()
}