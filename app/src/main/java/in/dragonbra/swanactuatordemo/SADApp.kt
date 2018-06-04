package `in`.dragonbra.swanactuatordemo

import `in`.dragonbra.swanactuatordemo.component.DaggerSADComponent
import `in`.dragonbra.swanactuatordemo.component.SADComponent
import `in`.dragonbra.swanactuatordemo.module.AppModule
import `in`.dragonbra.swanactuatordemo.module.ViewModelModule
import android.app.Application
import android.support.v4.app.Fragment

class SADApp : Application() {

    lateinit var graph: SADComponent

    override fun onCreate() {
        super.onCreate()

        graph = DaggerSADComponent.builder()
                .appModule(AppModule(this))
                .viewModelModule(ViewModelModule())
                .build()
    }
}

fun Fragment.graph() = (this.activity?.application as? SADApp)?.graph
