package com.calcprojects.constructorbuddy.ui.calculator

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*

import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Material
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.model.Shape
import com.calcprojects.constructorbuddy.model.Shape.*
import com.calcprojects.constructorbuddy.ui.*
import kotlinx.android.synthetic.main.fragment_calculator.*

/**
 * A simple [Fragment] subclass.
 */
class CalculatorFragment : Fragment() {

    private lateinit var viewModel: CalcViewModel
    private lateinit var shape: Shape
    private lateinit var adapterRecShape: AdapterRecyclerShapes
    private val materials by lazy { Material.values() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalcViewModel::class.java)
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
        val startPosition = values().indexOf(shape)
        recycler_shapes_marked.initialize(adapterRecShape, startPosition, viewModel)


        viewModel.getShape().observe(viewLifecycleOwner, Observer {

            setIntroText(it)
            showFields(it)

        })


        indicator.attachToRecyclerView(recycler_shapes_marked)


        val adapter = AdapterSpinnerMat(requireContext(), R.layout.holder_material, materials)
        spinner.adapter = adapter

        spinner.setSelection(3)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

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

/*    private fun checkCompletion(): Boolean {
        return editText_field_1.warn() &&
                editText_field_2.warn() &&
                editText_field_3.warn() &&
                editText_field_4.warn() &&
                editText_field_5.warn()
    }*/

/*    private val EditText.warning: Boolean
        get() = if (visibility == View.VISIBLE) text.toString() != "" else true

    private fun EditText.warn(): Boolean {
        val warn = if (visibility == View.VISIBLE) {
            text.toString() != ""
        } else true
        if (warn) hint = requireContext().getString(R.string.inputError)
        return warn
    }

    private fun EditText.clearWarn() {

    }*/

    private fun clearInput() {
        etField1.text.clear()
        etField2.text.clear()
        etField3.text.clear()
        etField4.text.clear()
        etField5.text.clear()
    }

    private fun calculate(shape: Shape, material: Material): Model? {

        val res1: Double? by lazy { etField1.getValue() }
        val res2: Double? by lazy { etField2.getValue() }
        val res3: Double? by lazy { etField3.getValue() }
        val res4: Double? by lazy { etField4.getValue() }
        val res5: Double? by lazy { etField5.getValue() }

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

    private fun ConstraintLayout.hide() = run { visibility = View.GONE }

    private fun ConstraintLayout.show() = run { visibility = View.VISIBLE }

    private fun showFields(shape: Shape) {

        layInp1.show()
        layInp2.show()
        when (shape) {

            ANGLE -> {
                layInp3.show();layInp4.show();layInp5.hide()
            }
            T_BAR -> {
                layInp3.show();layInp4.show();layInp5.hide()
            }
            SQUARE_TUBE -> {
                layInp3.show();layInp4.show();layInp5.hide()
            }
            SQUARE_BAR -> {
                layInp3.hide();layInp4.hide();layInp5.hide()
            }
            ROUND_BAR -> {
                layInp3.hide();layInp4.hide();layInp5.hide()
            }
            PIPE -> {
                layInp3.show();layInp4.hide();layInp5.hide()
            }
            HEXAGONAL_TUBE -> {
                layInp3.show();layInp4.hide();layInp5.hide()
            }
            HEXAGONAL_BAR -> {
                layInp3.hide();layInp4.hide();layInp5.hide()
            }
            HEXAGONAL_HEX -> {
                layInp3.show();layInp4.hide();layInp5.hide()
            }
            FLAT_BAR -> {
                layInp3.show();layInp4.hide();layInp5.hide()
            }
            CHANNEL -> {
                layInp3.show();layInp4.show();layInp5.hide()
            }
            BEAM -> {
                layInp3.show();layInp4.show();layInp5.show()
            }
        }
    }

    private fun setIntroText(shape: Shape) {

        val length: String by lazy { requireContext().getString(R.string.length) + " (L):" }
        val width: String by lazy { requireContext().getString(R.string.width) + " (W):" }
        val height: String by lazy { requireContext().getString(R.string.height) + " (H):" }
        val diameter: String by lazy { requireContext().getString(R.string.diameter) + " (D):" }
        val thickness: String by lazy { requireContext().getString(R.string.thickness) }
        val side: String by lazy { requireContext().getString(R.string.side) + " (S):" }

        tvField1.text = length
        tvField2.text = when (shape) {
            PIPE, ROUND_BAR, HEXAGONAL_TUBE -> diameter
            ANGLE, BEAM, CHANNEL, FLAT_BAR, HEXAGONAL_HEX, SQUARE_TUBE, T_BAR -> width
            HEXAGONAL_BAR -> height
            SQUARE_BAR -> side
        }
        tvField3.text = when (shape) {
            ANGLE, BEAM, CHANNEL, FLAT_BAR, SQUARE_TUBE, T_BAR -> height
            PIPE, HEXAGONAL_HEX -> "$thickness (T):"
            HEXAGONAL_TUBE -> side
            HEXAGONAL_BAR, SQUARE_BAR, ROUND_BAR -> ""
        }
        tvField4.text = when (shape) {
            ANGLE, CHANNEL, SQUARE_TUBE, T_BAR -> "$thickness (T):"
            BEAM -> "$thickness (T1):"
            FLAT_BAR, HEXAGONAL_BAR, HEXAGONAL_HEX, HEXAGONAL_TUBE, PIPE, ROUND_BAR, SQUARE_BAR -> ""
        }
        tvField5.text = if (shape == BEAM) "$thickness (T2):" else ""


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


    private fun RecyclerView.initialize(
        adapter: AdapterRecyclerShapes,
        startPosition: Int,
        viewModel: CalcViewModel
    ) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        this.adapter = adapter
        scrollToPosition(startPosition)
        this.attachSnapHelperWithListener(
            PagerSnapHelper(),
            onSnapPositionChangeListener = object : OnSnapPositionChangeListener {
                override fun onSnapPositionChange(position: Int) {
                    viewModel.setShape(Shape.values()[position])
                }
            })
    }

}
