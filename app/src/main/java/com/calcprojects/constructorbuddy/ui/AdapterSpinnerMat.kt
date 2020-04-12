package com.calcprojects.constructorbuddy.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Material


class AdapterSpinnerMat(context: Context, res: Int, materials: Array<Material>) :
    ArrayAdapter<Material>(context, res, materials) {

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

        val image: ImageView = view.findViewById(R.id.circle_image_material)
        val name: TextView = view.findViewById(R.id.textView_material_name)
        val density: TextView = view.findViewById(R.id.textView_material_density)

        val mat = getItem(position)
        mat?.run {
            image.setImageResource(imageRes)
            name.text = context.getString(nameRes)
            val densText = "density: ${this.density} $unit"
            density.text = densText
        }
        return view
    }
}
