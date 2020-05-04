package com.calcprojects.constructorbuddy.ui

import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Model
import com.google.android.material.appbar.AppBarLayout
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.StringBuilder


class AdapterRecyclerSaved(
    private val activity: FragmentActivity,
    var models: List<Model>?,
    private val appBar: AppBarLayout,
    private val funcOpenModel: (Int) -> Unit,
    private val funcRemoveModels: (List<Int>) -> Unit
) :
    RecyclerView.Adapter<AdapterRecyclerSaved.Holder>() {

    private var actionMode: ActionMode? = null
    private var selectedList: ArrayList<Int>? = null

    inner class Holder(
        itemView: View,
        funcOpenModel: (Int) -> Unit,
        funcRemoveModels: (List<Int>) -> Unit
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

                selectedList = arrayListOf()
                (selectedList as ArrayList<Int>).add(adapterPosition)
                mode?.menuInflater?.inflate(R.menu.menu_contextual, menu)
                notifyDataSetChanged()
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.share -> {
                        models?.let { m ->
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND

                                val textList =
                                    selectedList?.map { adPos -> m[adPos].getResultToSend(activity) }
                                val text = textList?.run {
                                    val strBuilder = StringBuilder()
                                    forEach { strBuilder.append(it) }
                                    strBuilder.toString()
                                }
                                putExtra(Intent.EXTRA_TEXT, text)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            activity.startActivity(shareIntent)
                        }
                        mode?.finish()
                        true
                    }
                    R.id.delete -> {

                        models?.run {
                            val arrayOfModelId = selectedList?.map { adPos -> this[adPos].id }
                            arrayOfModelId?.let { funcRemoveModels(it) }
                        }
                        mode?.finish()
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                selectedList = null
                actionMode = null
                setActivityState(actionMode)
                notifyDataSetChanged()
            }
        }

        init {
            itemView.setOnLongClickListener {
                return@setOnLongClickListener if (actionMode != null) false else {
                    actionMode = (activity as AppCompatActivity).startSupportActionMode(callback)
                    setActivityState(actionMode)
                    true
                }
            }

            itemView.setOnClickListener {
                models?.let { mod ->
                    selectedList?.run {
                        if (contains(adapterPosition)) remove(adapterPosition)
                        else add(adapterPosition)
                        notifyDataSetChanged()
                    } ?: funcOpenModel(mod[adapterPosition].id)
                }
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_saved_models, parent, false)
        return Holder(view, funcOpenModel, funcRemoveModels)
    }

    override fun getItemCount(): Int = models?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.pressState(selectedList, position)
    }

    private fun Holder.pressState(selectedList: ArrayList<Int>?, position: Int) {
        models?.run {
            name.text = (position + 1).toString()
            image.setImageResource(this[position].shape.form.imageRes)
            coverLayout.visibility = selectedList?.run {

                iconChecked.setImageResource(
                    if (contains(position)) R.drawable.ic_check
                    else R.drawable.ic_uncheck
                )
                View.VISIBLE
            } ?: View.GONE
        }
    }

    private fun setActivityState(actionMode: ActionMode?) {
        if (actionMode != null) {
            MainViewModel.showBottomActionView(show = false, withAnimation = true)
            appBar.setPadding(0, -108, 0, 0)
        } else {
            MainViewModel.showBottomActionView(show = true, withAnimation = true)
            appBar.setPadding(0, 48, 0, 0)
        }
    }
}
