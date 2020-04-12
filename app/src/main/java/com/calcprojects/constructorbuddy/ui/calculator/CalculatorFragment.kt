package com.calcprojects.constructorbuddy.ui.calculator

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Material
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.model.Shape
import com.calcprojects.constructorbuddy.model.Shape.*
import com.calcprojects.constructorbuddy.ui.AdapterRecyclerShapes
import com.calcprojects.constructorbuddy.ui.AdapterSpinnerMat
import com.calcprojects.constructorbuddy.ui.DEF_VALUE
import com.calcprojects.constructorbuddy.ui.SHAPE_KEY
import kotlinx.android.synthetic.main.fragment_calculator.*

/**
 * A simple [Fragment] subclass.
 */
class CalculatorFragment : Fragment() {


    private lateinit var shape: Shape
    private lateinit var adapterRecShape: AdapterRecyclerShapes
    private val materials by lazy { Material.values() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shape = valueOf(arguments!!.getString(SHAPE_KEY, DEF_VALUE))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterRecShape = AdapterRecyclerShapes(requireContext(), true) {}
        recycler_shapes_marked.setHasFixedSize(true)
        recycler_shapes_marked.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recycler_shapes_marked.adapter = adapterRecShape
        recycler_shapes_marked.scrollToPosition(values().indexOf(shape))

        indicator.attachToRecyclerView(recycler_shapes_marked)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recycler_shapes_marked)

        val adapter = AdapterSpinnerMat(requireContext(),R.layout.holder_material, materials)
        spinner.adapter = adapter

        spinner.setSelection(3)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                Log.d("asasa", "POS: $position")
            }
        }

    }


/*
         override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
             super.onViewCreated(view, savedInstanceState)

             shape?.let {
                 imageView_sel_shape.setImageResource(it.markedImageRes)
                 showFields(it)
                 setIntroText(it)
             }
             material?.let {
                 imageView_sel_material.setImageResource(it.imageRes)
                 textView_material_name_show.text = requireContext().getString(it.nameRes)
                 val density =
                     requireContext().getString(R.string.density) + ": ${it.density} ${it.unit}"
                 textView_density_show.text = density
             }

             viewModel.getModel().observe(viewLifecycleOwner, Observer {
                 it?.run {
                     layout_fields_result.visibility = View.VISIBLE
                     textView_result_weight_length.text = weight.toString()
                     textView_result_volume.text = volume.toString()
                 } ?: run {
                     layout_fields_result.visibility = View.GONE
                 }
             })

             button_calculate.setOnClickListener {
                 if (checkCompletion()) {
                     val model = shape?.let { s -> material?.let { m -> calculate(s, m) } }
                     viewModel.setModel(model)
                 }
             }

             button_clear.setOnClickListener {
                 viewModel.setModel(null)
                 clearInput()
             }
         }
*/

    private fun checkCompletion(): Boolean {
        return editText_field_1.warn() &&
                editText_field_2.warn() &&
                editText_field_3.warn() &&
                editText_field_4.warn() &&
                editText_field_5.warn()
    }

    private val EditText.warning: Boolean
        get() = if (visibility == View.VISIBLE) text.toString() != "" else true

    private fun EditText.warn(): Boolean {
        val warn = if (visibility == View.VISIBLE) {
            text.toString() != ""
        } else true
        if (warn) hint = requireContext().getString(R.string.inputError)
        return warn
    }

    private fun EditText.clearWarn() {

    }

    private fun clearInput() {
        editText_field_1.text.clear()
        editText_field_2.text.clear()
        editText_field_3.text.clear()
        editText_field_4.text.clear()
        editText_field_5.text.clear()
    }

    private fun calculate(shape: Shape, material: Material): Model? {

        val res1: Double? by lazy { editText_field_1.getValue() }
        val res2: Double? by lazy { editText_field_2.getValue() }
        val res3: Double? by lazy { editText_field_3.getValue() }
        val res4: Double? by lazy { editText_field_4.getValue() }
        val res5: Double? by lazy { editText_field_5.getValue() }

        return res1?.let { r1 ->
            res2?.let { r2 ->
                Model.Builder
                    .ByLength(r1)
                    .material(material)
                    .shape(shape)
                    .param2(r2).param3(res3).param4(res4).param5(res5)
                    .build()
            }
        }

    }

    private fun EditText.getValue(): Double? {
        return if (text.toString() != "") text.toString().toDouble() else null
    }

    private fun ConstraintLayout.hide() {
        visibility = View.GONE
    }

    private fun showFields(shape: Shape) {
        when (shape) {
            ANGLE -> layout_input_5.hide()
            T_BAR -> layout_input_5.hide()
            SQUARE_TUBE -> layout_input_5.hide()
            SQUARE_BAR -> {
                layout_input_3.hide(); layout_input_4.hide(); layout_input_5.hide()
            }
            ROUND_BAR -> {
                layout_input_3.hide(); layout_input_4.hide(); layout_input_5.hide()
            }
            PIPE -> {
                layout_input_4.hide(); layout_input_5.hide()
            }
            HEXAGONAL_TUBE -> {
                layout_input_4.hide(); layout_input_5.hide()
            }
            HEXAGONAL_BAR -> {
                layout_input_3.hide(); layout_input_4.hide(); layout_input_5.hide()
            }
            HEXAGONAL_HEX -> {
                layout_input_4.hide(); layout_input_5.hide()
            }
            FLAT_BAR -> {
                layout_input_4.hide(); layout_input_5.hide()
            }
            CHANNEL -> layout_input_5.hide()
            BEAM -> Unit
        }
    }

    private fun setIntroText(shape: Shape) {

        val length: String by lazy { requireContext().getString(R.string.length) + " (L):" }
        val width: String by lazy { requireContext().getString(R.string.width) + " (W):" }
        val height: String by lazy { requireContext().getString(R.string.height) + " (H):" }
        val diameter: String by lazy { requireContext().getString(R.string.diameter) + " (D):" }
        val thickness: String by lazy { requireContext().getString(R.string.thickness) }
        val side: String by lazy { requireContext().getString(R.string.side) + " (S):" }

        textView_field_1.text = length
        textView_field_2.text = when (shape) {
            PIPE, ROUND_BAR, HEXAGONAL_TUBE -> diameter
            ANGLE, BEAM, CHANNEL, FLAT_BAR, HEXAGONAL_HEX, SQUARE_TUBE, T_BAR -> width
            HEXAGONAL_BAR -> height
            SQUARE_BAR -> side
        }
        textView_field_3.text = when (shape) {
            ANGLE, BEAM, CHANNEL, FLAT_BAR, SQUARE_TUBE, T_BAR -> height
            PIPE, HEXAGONAL_HEX -> "$thickness (T):"
            HEXAGONAL_TUBE -> side
            HEXAGONAL_BAR, SQUARE_BAR, ROUND_BAR -> ""
        }
        textView_field_4.text = when (shape) {
            ANGLE, CHANNEL, SQUARE_TUBE, T_BAR -> "$thickness (T):"
            BEAM -> "$thickness (T1):"
            FLAT_BAR, HEXAGONAL_BAR, HEXAGONAL_HEX, HEXAGONAL_TUBE, PIPE, ROUND_BAR, SQUARE_BAR -> ""
        }
        if (shape == BEAM) {
            val text = "$thickness (T2):"
            textView_field_5.text = text
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("hkjg", "CALC.FR _ onActivityCreated")

    }

    override fun onStart() {
        super.onStart()
        Log.d("hkjg", "CALC.FR _ onStart")
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.d("hkjg", "CALC.FR _ onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("hkjg", "CALC.FR _ onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hkjg", "CALC.FR _ onDestroyView")

    }

}
