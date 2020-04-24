package com.calcprojects.constructorbuddy.ui.calculator

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*

import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.figures.Substance
import com.calcprojects.constructorbuddy.model.figures.Form
import com.calcprojects.constructorbuddy.model.figures.Form.*
import com.calcprojects.constructorbuddy.ui.StateUIActivity
import com.calcprojects.constructorbuddy.ui.*
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 */


class CalculatorFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Main + Job()
    private val state: StateUIActivity by lazy {
        StateUIActivity(
            View.SYSTEM_UI_FLAG_VISIBLE,
            false,
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        )
    }
    private lateinit var viewModel: CalcViewModel
    private var shape: Form? = null
    private lateinit var adapterRecShape: AdapterRecyclerShapes
    private val materials by lazy { Substance.values() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()
        launch {
            delay(SCREEN_DELAY_TIME.toLong())
        }

        Log.d("hhas", "CALC: onCreate()")


        viewModel = ViewModelProvider(this).get(CalcViewModel::class.java)
        val shapeName = arguments?.let {
            CalculatorFragmentArgs.fromBundle(it).shapeSelected
        }
        shape = shapeName?.let { Form.valueOf(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        Log.d("hhas", "CALC: onCreateView()")

        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("hhas", "CALC: onViewCreated()")

        //recycler init
        adapterRecShape = AdapterRecyclerShapes(requireContext(), true) {}
        recycler_shapes_marked.initialize(
            adapterRecShape, values().indexOf(shape), viewModel, indicator
        )

        //spinner init
        val adapter = AdapterSpinnerMat(requireContext(), materials = materials)
        spinner.initialize(adapter, viewModel)

        //editText init
      /*  etField1.initWithTextView(tvField1)
        etField2.initWithTextView(tvField2)*/
      /*  etField3.initWithTextView(tvField3)
        etField4.initWithTextView(tvField4)
        etField5.initWithTextView(tvField5)*/

        btn_byLength.setOnClickListener {
            viewModel.setType(true)
        }

        btn_byWeight.setOnClickListener {
            viewModel.setType(false)
        }

        btn_calculate.setOnClickListener {
            /*     val par1 = etField1.getValue(layInp1, tvField1)
                 val par2 = etField2.getValue(layInp2, tvField2)
                 val par3 = etField3.getValue(layInp3, tvField3)
                 val par4 = etField4.getValue(layInp4, tvField4)
                 val par5 = etField5.getValue(layInp5, tvField5)
                 if (par1 != NO_INPUT && par2 != NO_INPUT && par3 != NO_INPUT && par4 != NO_INPUT
                     && par5 != NO_INPUT && par1 != null && par2 != null
                 )
                     viewModel.setParameters(par1, par2, par3, par4, par5)*/

            it.findNavController().navigate(CalculatorFragmentDirections.actionShowResult())

        }

     /*   btn_clear.setOnClickListener {
            it.findNavController().navigate(CalculatorFragmentDirections.actionOpenSettings())
        }*/

        viewModel.getType().observe(viewLifecycleOwner, Observer {

            btn_byLength.setSelectedOption(it)
            btn_byWeight.setSelectedOption(!it)

          /*  tvField1.text =
                if (it) requireContext().getString(R.string.length) + " (L):"
                else requireContext().getString(R.string.weight) + " (Wg):"
            etField1.text.clear()
            tvField1.warning(false)*/
        })

        viewModel.getShape().observe(viewLifecycleOwner, Observer
        {
            /*setIntroText(it)
            showFields(it)*/
        })

        viewModel.getModel().observe(viewLifecycleOwner, Observer {

            Log.d("asafe", "in Observer: weight: ${it?.weight}")

            /*it?.run {
                tv_result_weight_length.text =
                    if (btn_byLength.tag == SELECTED)
                        "${requireContext().getString(R.string.weight)} = ${decFormatter2p.format(
                            weight
                        )}"
                    else "${requireContext().getString(R.string.length)} = ${decFormatter2p.format(
                        length
                    )}"

                Log.d("asafe", "in Observer: length: ${it.length}")

            }*/
        })

    }

    override fun onStart() {
        super.onStart()
        Log.d("hhas", "CALC: onStart()")

    }

    override fun onPause() {
        super.onPause()
        Log.d("hhas", "CALC: onPause()")

    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeSources()
        Log.d("hhas", "CALC: onDestroyView()")

    }

  /*  private fun clearInput() {
        etField1.text.clear()
        etField2.text.clear()
        etField3.text.clear()
        etField4.text.clear()
        etField5.text.clear()
        tvField1.warning(false)
        tvField2.warning(false)
        tvField3.warning(false)
        tvField4.warning(false)
        tvField5.warning(false)
    }*/

    private fun EditText.getValue(layout: ConstraintLayout, tv: TextView): Double? {
        return if (layout.visibility == View.VISIBLE) {
            if (text.toString() != "") text.toString().toDouble() else {
                tv.warning(true)
                NO_INPUT
            }
        } else null
    }

    private fun ConstraintLayout.hide() = run { visibility = View.GONE }

    private fun ConstraintLayout.show() = run { visibility = View.VISIBLE }

  /*  private fun showFields(shape: Shape) {

        clearInput()
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

        val width: String by lazy { requireContext().getString(R.string.width) + " (W):" }
        val height: String by lazy { requireContext().getString(R.string.height) + " (H):" }
        val diameter: String by lazy { requireContext().getString(R.string.diameter) + " (D):" }
        val thickness: String by lazy { requireContext().getString(R.string.thickness) }
        val side: String by lazy { requireContext().getString(R.string.side) + " (S):" }

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

    }*/

    private fun RecyclerView.initialize(
        adapter: AdapterRecyclerShapes,
        startPosition: Int? = null,
        viewModel: CalcViewModel? = null,
        indicator: IndefinitePagerIndicator? = null
    ) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        this.adapter = adapter
        startPosition?.run { scrollToPosition(this) }
        this.attachSnapHelperWithListener(
            PagerSnapHelper(),
            onSnapPositionChangeListener = object : OnSnapPositionChangeListener {
                override fun onSnapPositionChange(position: Int) {
                    viewModel?.setShape(Form.values()[position])
                }
            })
        indicator?.attachToRecyclerView(this)
    }

    private fun Spinner.initialize(
        adapter: AdapterSpinnerMat,
        viewModel: CalcViewModel? = null,
        startPosition: Int? = null
    ) {
        this.adapter = adapter
        startPosition?.run { setSelection(this) }
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel?.setMaterial(materials[position])
            }
        }
    }

    private fun EditText.initWithTextView(tv: TextView) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tv.warning(false)
            }
        })
    }

    private fun Button.setSelectedOption(selected: Boolean) {
        background = if (selected) {
            tag = SELECTED
            context.getDrawable(R.drawable.background_btn_stay_pressed)
        } else {
            tag = UNSELECTED
            context.getDrawable(R.drawable.background_btn)
        }
    }

    private fun TextView.warning(warn: Boolean) {
        setTextColor(resources.getColor(if (warn) android.R.color.holo_red_dark else android.R.color.black))
    }


    override fun onResume() {
        super.onResume()
        Log.d("hhas", "CALC: onResume()")

        MainViewModel.setState(state)
    }

    override fun onStop() {
        super.onStop()
        Log.d("hhas", "CALC: onStop()")

        activity?.run { setTheme(R.style.NoActionTheme) }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hhas", "CALC: onDestroy()")
        job.cancel()

    }

}
