package `in`.dragonbra.swanactuatordemo.ui.main

import `in`.dragonbra.swanactuatordemo.SADApp
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import interdroid.swancore.swanmain.ActuatorManager
import interdroid.swancore.swanmain.ExpressionListener
import interdroid.swancore.swansong.TimestampedValue
import interdroid.swancore.swansong.TriState

class MainViewModel(private val context: SADApp) : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName

        private const val SENSOR_ID = "sad_lux"
        private const val ACTUATOR_ID = "sad_lux_vibr"
        private const val ACTUATOR_EXPRESSION = "self@light:lux{ANY,0}<10.0THEN" +
                "self@vibrator:vibrate?duration='500'&&" +
                "self@logger:log?tag='SwanActuatorDemo'#priority='3'#message='Low light detected'"
        private const val SENSOR_EXPRESSION = "self@light:lux?delay='20000'{ANY,0}THEN" +
                "self@http:post?" +
                "server_url='https://dragonbra.in/swan-act/'#" +
                "server_http_authorization='NoAuth'#" +
                "server_http_body_type='application/json'#" +
                "server_http_body='message:hi'"
    }

    val sensorValue: MutableLiveData<Float> = MutableLiveData()

    private val sensorListener = object : ExpressionListener {
        override fun onNewState(id: String?, timestamp: Long, newState: TriState?) {
            Log.d(TAG, "Sensor state: ${newState?.toString() ?: "null"}")
        }

        override fun onNewValues(id: String?, newValues: Array<out TimestampedValue>?) {
            if (newValues != null && newValues.isNotEmpty()) {
                sensorValue.value = newValues[0].value as Float
            }
        }
    }

    fun registerSensors() {
        ActuatorManager.unregisterActuator(context, ACTUATOR_ID, false)
        ActuatorManager.registerActuator(context, ACTUATOR_ID, ACTUATOR_EXPRESSION, sensorListener)

        ActuatorManager.unregisterActuator(context, SENSOR_ID, false)
        ActuatorManager.registerActuator(context, SENSOR_ID, SENSOR_EXPRESSION, sensorListener)
    }

    override fun onCleared() {
        ActuatorManager.unregisterActuator(context, ACTUATOR_ID, false)
        ActuatorManager.unregisterActuator(context, SENSOR_ID, false)
    }
}
