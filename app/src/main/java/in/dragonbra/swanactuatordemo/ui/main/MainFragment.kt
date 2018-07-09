package `in`.dragonbra.swanactuatordemo.ui.main

import `in`.dragonbra.swanactuatordemo.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import interdroid.swancore.swanmain.ActuatorInfo
import interdroid.swancore.swanmain.ActuatorManager
import kotlinx.android.synthetic.main.main_fragment.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class MainFragment : Fragment(), ActuatorAdapter.OnActuatorSelectedListener {

    companion object {
        val TAG: String = MainFragment::class.java.simpleName

        const val ACT_ID = "SwanActDemo"
    }

    private lateinit var availableActuators: MutableList<ActuatorInfo>

    private val actuators: MutableList<String> = ArrayList()

    private lateinit var adapter: ActuatorAdapter

    private var registered: Boolean = false

    private lateinit var registerMenuItem: MenuItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ActuatorAdapter(actuators, this)

        actuatorRecyclerView.layoutManager = LinearLayoutManager(context)
        actuatorRecyclerView.adapter = adapter

        availableActuators = ActuatorManager.getActuators(context!!)

        fabAdd.onClick {
            val entityIds = availableActuators.map { it.entityId }

            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Select actuator")
                    .setItems(entityIds.toTypedArray(), DialogInterface.OnClickListener { _, which ->
                        startActivityForResult(availableActuators[which].configurationIntent, 0)
                    })

            builder.show()
        }

        sensorExpressionInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                sensorExpressionText.text = buildExpression()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        sensorExpressionInput.setText("self@light:lux{ANY,0}<10.0")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        registerMenuItem = menu.findItem(R.id.register)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.register -> {
            if (registered) {
                unregister()
            } else {
                register()
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && data.hasExtra("expression")) {
            val expression = data.getStringExtra("expression")
            if (requestCode == 0) {
                actuators.add(expression)
            } else {
                actuators.set(requestCode - 1, expression)
            }
            adapter.notifyDataSetChanged()

            sensorExpressionText.text = buildExpression()
        }
    }

    override fun onActuatorEdit(position: Int, expression: String) {
        if (!registered) {
            val entity = expression.substring(expression.indexOf('@') + 1, expression.indexOf(':'))
            availableActuators.find { it.entityId == entity }?.let {
                val intent = it.configurationIntent

                intent.putExtra("expression", expression)
                startActivityForResult(intent, position + 1)
            }
        }
    }

    override fun onActuatorDelete(position: Int) {
        if (!registered) {
            actuators.removeAt(position)
            adapter.notifyDataSetChanged()
        }
    }

    private fun buildExpression(): String {
        val sensorExpression = sensorExpressionInput.text.toString()

        return if (actuators.isEmpty()) {
            sensorExpression
        } else {
            "${sensorExpression}THEN${actuators.joinToString("&&")}"
        }
    }

    private fun register() {
        unregister()
        if (!registered) {
            try {
                ActuatorManager.registerActuator(context, ACT_ID, buildExpression(), null)

                sensorExpressionInput.isEnabled = false
                fabAdd.hide()
                registered = true
                registerMenuItem.title = "Unregister"
            } catch (e: Exception) {
                Log.w(TAG, "failed to register expression", e)
            }
        }
    }

    private fun unregister() {
        if (registered) {
            ActuatorManager.unregisterActuator(context, ACT_ID, false)
            sensorExpressionInput.isEnabled = true

            fabAdd.show()
            registered = false
            registerMenuItem.title = "Register"
        }
    }
}
