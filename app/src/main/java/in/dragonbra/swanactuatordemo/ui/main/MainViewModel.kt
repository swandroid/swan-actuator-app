package `in`.dragonbra.swanactuatordemo.ui.main

import `in`.dragonbra.swanactuatordemo.SADApp
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import interdroid.swancore.swanmain.ActuatorManager
import interdroid.swancore.swanmain.ExpressionListener
import interdroid.swancore.swanmain.ExpressionManager
import interdroid.swancore.swansong.ExpressionFactory
import interdroid.swancore.swansong.TimestampedValue
import interdroid.swancore.swansong.TriState

class MainViewModel(private val context: SADApp) : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName

        private const val SENSOR_ID = "sad_lux"
        private const val ACTUATOR_ID = "sad_lux_vibr"
        private const val ACTUATOR_EXPRESSION = "self@light:lux{ANY,0}<1.0THENself@vibrator:vibrate?duration='500'"
        private val EXPRESSION = ExpressionFactory.parse("self@light:lux?delay='20000'{ANY,0}")
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
        ActuatorManager.unregisterActuator(context, ACTUATOR_ID)
        ActuatorManager.registerActuator(context, ACTUATOR_ID, ACTUATOR_EXPRESSION, sensorListener)

        ExpressionManager.unregisterExpression(context, SENSOR_ID)
        ExpressionManager.registerExpression(context, SENSOR_ID, EXPRESSION, sensorListener)
    }

    override fun onCleared() {
        ActuatorManager.unregisterActuator(context, ACTUATOR_ID)
        ExpressionManager.unregisterExpression(context, SENSOR_ID)
    }
}
