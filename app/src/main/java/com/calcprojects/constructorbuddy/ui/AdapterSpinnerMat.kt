package com.calcprojects.constructorbuddy.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.figures.Substance


class AdapterSpinnerMat(
    context: Context, res: Int = R.layout.holder_material, materials: Array<Substance>
) :
    ArrayAdapter<Substance>(context, res, materials) {

    private val resource = res
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(resource, parent, false)

        val name: TextView = view.findViewById(R.id.textView_material_name)
        val density: TextView = view.findViewById(R.id.textView_material_density)

        val mat = getItem(position)
        mat?.run {
            name.text = context.getString(nameRes)
            val densText = "  ${this.density} $unit"
            density.text = densText
        }
        return view
    }
}
