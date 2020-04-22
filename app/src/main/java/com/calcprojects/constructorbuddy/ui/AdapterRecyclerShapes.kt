package com.calcprojects.constructorbuddy.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.figures.Form


class AdapterRecyclerShapes(
    private val context: Context,
    private val isMarked: Boolean,
    private val func: (Form) -> Unit
) :
    RecyclerView.Adapter<AdapterRecyclerShapes.Holder>() {

    private var clicked = false
    var selectedPosition: Int? = null
    private val images: Array<Form> by lazy { Form.values() }

    inner class Holder(itemView: View, val func: (Form) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageView_shape)
        val name: TextView = itemView.findViewById(R.id.textView_shapeName)

        init {
            itemView.setOnClickListener {
                if (!clicked) {
                    func(images[adapterPosition])
                    selectedPosition = adapterPosition
                    notifyDataSetChanged()
                    clicked = true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.holder_shape, parent, false)
        return Holder(view, func)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (!isMarked) {
            holder.name.text = context.getString(images[position].nameRes)
            holder.animate(selectedPosition, position)
            holder.image.setImageResource(images[position].imageRes)
        } else
            holder.image.setImageResource(images[position].markedImageRes)

    }

    private fun Holder.animate(selPos: Int?, curPos: Int) {
        selPos?.let {
            if (curPos != it)
                itemView.animate().alpha(0.35F).scaleX(0.9F).scaleY(0.9F).apply { duration = 1000 }
            else
                itemView.animate().scaleX(1.2F).scaleY(1.2F).apply { duration = 1000 }
        }
    }
}
