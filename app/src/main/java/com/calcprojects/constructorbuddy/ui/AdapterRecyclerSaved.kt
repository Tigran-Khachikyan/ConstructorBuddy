package com.calcprojects.constructorbuddy.ui

import android.app.Activity
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Model
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import de.hdodenhof.circleimageview.CircleImageView


class AdapterRecyclerSaved(
    private val activity: FragmentActivity?,
    var models: List<Model>?,
    private val appBar: AppBarLayout,
    private val funcOpenModel: (Int) -> Unit,
    private val funcRemoveModel: (Int) -> Unit
) :
    RecyclerView.Adapter<AdapterRecyclerSaved.Holder>() {

    private var actionMode: ActionMode? = null

    inner class Holder(
        itemView: View,
        appBar: AppBarLayout,
        funcOpenModel: (Int) -> Unit,
        funcRemoveModel: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_saved_number)
        val material: TextView = itemView.findViewById(R.id.tv_saved_material)
        val date: TextView = itemView.findViewById(R.id.tv_saved_date)
        val image: CircleImageView = itemView.findViewById(R.id.iv_saved_shape)
        val coverLayout: ConstraintLayout = itemView.findViewById(R.id.lay_cover_holder_save)
        val iconChecked: ImageView = itemView.findViewById(R.id.iv_ic_checked)
        private val callback = object : ActionMode.Callback {

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                MainViewModel.showBottomActionView(show = false, withAnimation = true)
                coverLayout.visibility = View.VISIBLE
                appBar.layoutParams.height = 0
                iconChecked.setImageResource(R.drawable.ic_check)
                mode?.menuInflater?.inflate(R.menu.menu_contextual, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                Log.d("remmm", "when")

                return when (item?.itemId) {
                    R.id.share -> {
                        // Handle share icon press
                        mode?.finish()
                        true
                    }
                    R.id.delete -> {

                        models?.run {
                            val id = this[adapterPosition].id
                            Log.d("remmm", "id: $id")

                            funcRemoveModel(id)
                        }
                        mode?.finish()
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                MainViewModel.showBottomActionView(show = true, withAnimation = true)
                coverLayout.visibility = View.GONE
                appBar.layoutParams.height = AppBarLayout.LayoutParams.WRAP_CONTENT
                iconChecked.setImageResource(R.drawable.ic_uncheck)
                actionMode = null
            }
        }

        init {
            itemView.setOnLongClickListener {
                return@setOnLongClickListener if (actionMode != null) false else {
                    actionMode = (activity as AppCompatActivity).startSupportActionMode(callback)
                    true
                }
            }

            itemView.setOnClickListener {
                models?.run {
                    funcOpenModel(this[adapterPosition].id)
                }
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_saved_models, parent, false)
        return Holder(view, appBar, funcOpenModel, funcRemoveModel)
    }

    override fun getItemCount(): Int = models?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        models?.run {
            holder.name.text = (position + 1).toString()
            holder.image.setImageResource(this[position].shape.form.imageRes)
        }
    }
}
