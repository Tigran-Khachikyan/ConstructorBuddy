package com.calcprojects.constructorbuddy.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Shape


class AdapterRecyclerShapes(private val context: Context, private val func: (Shape) -> Unit) :
    RecyclerView.Adapter<AdapterRecyclerShapes.Holder>() {

    var selectedPosition: Int? = null
    private val images: Array<Shape> by lazy { Shape.values() }

    inner class Holder(itemView: View, val func: (Shape) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageView_shape)
        val name: TextView = itemView.findViewById(R.id.textView_shapeName)

        init {
            itemView.setOnClickListener {
                func(images[adapterPosition])
                selectedPosition = adapterPosition
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.holder_shape, parent, false)
        return Holder(view, func)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.name.text = context.getString(images[position].nameRes)
        holder.image.setImageResource(images[position].imageRes)
        holder.animate(selectedPosition, position)
    }

    private fun Holder.animate(selPos: Int?, curPos: Int) {
        selPos?.let {
            if (curPos != it)
                itemView.animate().alpha(0.35F).scaleX(0.8F).scaleY(0.8F).apply { duration = 1000 }
            else
                itemView.animate().scaleX(1.2F).scaleY(1.2F).apply { duration = 1000 }
        }
    }
}
