package com.calcprojects.constructorbuddy.ui.calculator

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*

import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.figures.Substance
import com.calcprojects.constructorbuddy.model.figures.Form
import com.calcprojects.constructorbuddy.model.figures.Form.*
import com.calcprojects.constructorbuddy.ui.*
import com.google.android.material.textfield.TextInputLayout
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class CalculatorFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Main + Job()

    private lateinit var viewModel: CalcViewModel
    private var form: Form? = null
    private lateinit var adapterRecShape: AdapterRecyclerShapes
    private val materials by lazy { Substance.values() }
    private var valueField1: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()

        val shapeName = arguments?.let {
            CalculatorFragmentArgs.fromBundle(it).shapeSelected
        }
        form = shapeName?.let { valueOf(it) }
        viewModel = ViewModelProvider(this).get(CalcViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //recycler init
        adapterRecShape = AdapterRecyclerShapes(requireContext(), true) {}
        recycler_shapes_marked.initialize(adapterRecShape, viewModel, indicator)
        form?.let {
            viewModel.setForm(it)
            recycler_shapes_marked.scrollToPosition(values().indexOf(it))
        }

        //spinner init
        val adapter = AdapterSpinnerMat(requireContext(), materials = materials)
        spinner.initialize(adapter, viewModel)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radByLength -> viewModel.setType(true)
                R.id.radByWeight -> viewModel.setType(false)
            }
        }

        btn_calculate.setOnClickListener {
            val par1 = field1.getValue()
            val par2 = field2.getValue()
            val par3 = field3.getValue()
            val par4 = field4.getValue()
            val par5 = field5.getValue()
            if (
                par1 != null && par1 != NO_INPUT
                && par2 != null && par2 != NO_INPUT
                && par3 != NO_INPUT
                && par4 != NO_INPUT
                && par5 != NO_INPUT
            ) {
                valueField1 = par1
                viewModel.setParameters(par1, par2, par3, par4, par5)
                val success = viewModel.calculate()
                if (success) {
                    it.findNavController().navigate(CalculatorFragmentDirections.actionShowResult())
                }
            }
        }





        viewModel.getType().observe(viewLifecycleOwner, Observer {

            field1.hint =
                if (it) requireContext().getString(R.string.length) + " (L)"
                else requireContext().getString(R.string.weight) + " (Wg)"
            field1.editText?.text?.clear()
            field1.clearFocus()
            field1.isErrorEnabled = false
        })

        viewModel.getForm().observe(viewLifecycleOwner, Observer
        {
            setTextInputLayoutHintText(it)
            setTextInputLayoutVisibility(it)
        })

        topAppBar_calc_fragment.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toSettings -> findNavController().navigate(CalculatorFragmentDirections.actionOpenSettings())
                R.id.toHome -> activity?.onBackPressed()
                R.id.toSaved -> findNavController().navigate(CalculatorFragmentDirections.actionToSavedModels())
            }
            return@setOnMenuItemClickListener false
        }

        field1.editText?.doOnTextChanged { _, _, _, _ ->
            field1.isErrorEnabled = false
        }
        field2.editText?.doOnTextChanged { _, _, _, _ ->
            field2.isErrorEnabled = false
        }
        field3.editText?.doOnTextChanged { _, _, _, _ ->
            field3.isErrorEnabled = false
        }
        field4.editText?.doOnTextChanged { _, _, _, _ ->
            field4.isErrorEnabled = false
        }
        field5.editText?.doOnTextChanged { _, _, _, _ ->
            field5.isErrorEnabled = false
        }

    }

    private fun TextInputLayout.getValue(): Double? {
        return if (visibility == View.VISIBLE) {
            if (editText?.text?.toString() != "") {
                editText?.text.toString().toDouble()
            } else {
                isErrorEnabled = true
                error = resources.getString(R.string.error_text)
                NO_INPUT
            }
        } else null
    }

    private fun TextInputLayout.hide() = run {
        //  editText?.text?.clear()
        clearFocus()
        visibility = View.GONE
    }

    private fun TextInputLayout.show() = run { visibility = View.VISIBLE }

    private fun setTextInputLayoutVisibility(form: Form) {

        field1.isErrorEnabled = false
        field2.isErrorEnabled = false
        field3.isErrorEnabled = false
        field4.isErrorEnabled = false
        field5.isErrorEnabled = false
        field1.show()
        field2.show()

        when (form) {
            ANGLE -> {
                field3.show();field4.show();field5.hide()
            }
            T_BAR -> {
                field3.show();field4.show();field5.hide()
            }
            SQUARE_TUBE -> {
                field3.show();field4.show();field5.hide()
            }
            SQUARE_BAR -> {
                field3.hide();field4.hide();field5.hide()
            }
            ROUND_BAR -> {
                field3.hide();field4.hide();field5.hide()
            }
            PIPE -> {
                field3.show();field4.hide();field5.hide()
            }
            HEXAGONAL_TUBE -> {
                field3.show();field4.hide();field5.hide()
            }
            HEXAGONAL_BAR -> {
                field3.hide();field4.hide();field5.hide()
            }
            HEXAGONAL_HEX -> {
                field3.show();field4.hide();field5.hide()
            }
            FLAT_BAR -> {
                field3.show();field4.hide();field5.hide()
            }
            CHANNEL -> {
                field3.show();field4.show();field5.hide()
            }
            BEAM -> {
                field3.show();field4.show();field5.show()
            }
        }
    }

    private fun setTextInputLayoutHintText(form: Form) {

        val width: String by lazy { requireContext().getString(R.string.width) + " (W)" }
        val height: String by lazy { requireContext().getString(R.string.height) + " (H)" }
        val diameter: String by lazy { requireContext().getString(R.string.diameter) + " (D)" }
        val thickness: String by lazy { requireContext().getString(R.string.thickness) }
        val side: String by lazy { requireContext().getString(R.string.side) + " (S)" }

        field2.hint = when (form) {
            PIPE, ROUND_BAR, HEXAGONAL_TUBE -> diameter
            ANGLE, BEAM, CHANNEL, FLAT_BAR, HEXAGONAL_HEX, SQUARE_TUBE, T_BAR -> width
            HEXAGONAL_BAR -> height
            SQUARE_BAR -> side
        }
        field3.hint = when (form) {
            ANGLE, BEAM, CHANNEL, FLAT_BAR, SQUARE_TUBE, T_BAR -> height
            PIPE, HEXAGONAL_HEX -> "$thickness (T)"
            HEXAGONAL_TUBE -> side
            HEXAGONAL_BAR, SQUARE_BAR, ROUND_BAR -> ""
        }
        field4.hint = when (form) {
            ANGLE, CHANNEL, SQUARE_TUBE, T_BAR -> "$thickness (T)"
            BEAM -> "$thickness (T1)"

            FLAT_BAR, HEXAGONAL_BAR, HEXAGONAL_HEX, HEXAGONAL_TUBE, PIPE, ROUND_BAR, SQUARE_BAR -> ""
        }
        field5.hint = if (form == BEAM) "$thickness (T2)" else ""

    }

    private fun RecyclerView.initialize(
        adapter: AdapterRecyclerShapes,
        viewModel: CalcViewModel? = null,
        indicator: IndefinitePagerIndicator? = null
    ) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        this.adapter = adapter
        this.attachSnapHelperWithListener(
            PagerSnapHelper(),
            onSnapPositionChangeListener = object : OnSnapPositionChangeListener {
                override fun onSnapPositionChange(position: Int) {
                    viewModel?.setForm(values()[position])
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
                viewModel?.setSubstance(materials[position])
            }
        }
    }

    override fun onResume() {
        super.onResume()

        valueField1?.let { field1.editText?.setText(it.toString()) }
        configureActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun configureActivity() {
        activity?.run {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        MainViewModel.showBottomActionView(false)
    }

}
