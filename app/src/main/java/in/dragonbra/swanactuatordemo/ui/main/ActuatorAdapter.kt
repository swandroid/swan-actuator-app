package `in`.dragonbra.swanactuatordemo.ui.main

import `in`.dragonbra.swanactuatordemo.R
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.actuator_list_item.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class ActuatorAdapter(private val actuators: MutableList<String>, private val listener: OnActuatorSelectedListener? = null) : RecyclerView.Adapter<ActuatorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.actuator_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = actuators.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        fun bind(position: Int) {
            val expression = actuators[position]

            v.actuator.text = expression

            v.delete.onClick {
                listener?.onActuatorDelete(position)
            }

            v.edit.onClick {
                listener?.onActuatorEdit(position, expression)
            }
        }
    }

    interface OnActuatorSelectedListener {
        fun onActuatorEdit(position: Int, expression: String)
        fun onActuatorDelete(position: Int)
    }
}