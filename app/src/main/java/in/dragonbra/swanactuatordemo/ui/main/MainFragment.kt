package `in`.dragonbra.swanactuatordemo.ui.main

import `in`.dragonbra.swanactuatordemo.R
import `in`.dragonbra.swanactuatordemo.graph
import `in`.dragonbra.swanactuatordemo.viewmodel.SADViewModelProvider
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var provider: SADViewModelProvider

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        graph()?.inject(this)

        viewModel = ViewModelProviders.of(this, provider).get(MainViewModel::class.java)

        viewModel.sensorValue.observe(this, Observer {
            it?.let {
                message.text = "Value = $it lux"
            }
        })
    }

}
