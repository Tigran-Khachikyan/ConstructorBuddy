package com.calcprojects.constructorbuddy.ui.saved

import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Model
import de.hdodenhof.circleimageview.CircleImageView


class AdapterRecyclerSaved(
    val context: Context,
    var models: List<Model>?,
    private val holderClickListener: OnHolderClickListener
) : RecyclerView.Adapter<AdapterRecyclerSaved.Holder>() {

    private var selectedModels: List<Model>? = null
    fun setSelectedList(models: List<Model>?) {
        selectedModels = models
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_saved_models, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = models?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        models?.run { holder.bind(get(position)) }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.tv_saved_number)
        private val material: TextView = itemView.findViewById(R.id.tv_saved_material)
        private val date: TextView = itemView.findViewById(R.id.tv_saved_date)
        private val image: CircleImageView = itemView.findViewById(R.id.iv_saved_shape)
        private val coverLayout: ConstraintLayout =
            itemView.findViewById(R.id.lay_cover_holder_save)
        private val iconChecked: ImageView = itemView.findViewById(R.id.iv_ic_checked)

        fun bind(model: Model) {

            name.text = "${(adapterPosition + 1)}"
            image.setImageResource(model.shape.form.imageRes)
            date.text = model.dateOfCreation
            material.text = context.resources.getString(model.material.substance.nameRes)

            coverLayout.visibility = if (selectedModels != null) View.VISIBLE else View.GONE
            selectedModels?.run {
                iconChecked.setImageResource(
                    if (contains(model)) R.drawable.ic_check
                    else R.drawable.ic_uncheck
                )
            }

            itemView.setOnClickListener {
                holderClickListener.onHolderClick(model)
            }

            itemView.setOnLongClickListener {
                holderClickListener.onHolderLongClick(model)
            }
        }
    }

}
