package com.calcprojects.constructorbuddy.ui.calculator

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.figures.Form
import com.calcprojects.constructorbuddy.model.figures.Form.*
import com.calcprojects.constructorbuddy.model.figures.Substance
import com.calcprojects.constructorbuddy.model.price.Currency
import com.calcprojects.constructorbuddy.model.price.Price
import com.calcprojects.constructorbuddy.model.units.Unit
import com.calcprojects.constructorbuddy.ui.*
import com.calcprojects.constructorbuddy.ui.result.ResultViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CalculatorFragment : Fragment(), ScreenConfigurations,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var preferences: SharedPreferences
    private lateinit var viewModel: CalculationViewModel
    private var resultViewModel: ResultViewModel? = null
    private lateinit var adapterRecShape: AdapterRecyclerShapes
    private val materials by lazy { Substance.values() }
    private var valueField1: Double? = null
    private var isUnitDefMetric: Boolean? = null
    private lateinit var adapterSpinner: AdapterSpinner
    private var jobComputing: Job? = null

    override fun onCreateView(inf: LayoutInflater, con: ViewGroup?, save: Bundle?): View? {

        viewModel = ViewModelProvider(this).get(CalculationViewModel::class.java)
        resultViewModel = activity?.let { ViewModelProvider(it).get(ResultViewModel::class.java) }
        return inf.inflate(R.layout.fragment_calculator, con, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPreferenceWithDefaultValues()
        recycler_shapes_marked.initWithListeners()
        spinnerMat.initWithListeners()
        radioGroup.initWithListeners()
        topAppBar_calc_fragment.setMenuListener()
        initButtonCalculate()
        getSelectedForm()

        viewModel.getTypeAndUnit().observe(viewLifecycleOwner, Observer {

            it?.run {
                val byLength = first
                val metric = second

                field1.hint =
                    if (byLength) requireContext().getString(R.string.length) + " (L)"
                    else requireContext().getString(R.string.weight) + " (Wg)"

                field1.editText?.text?.clear()
                field1.clearFocus()
                field1.isErrorEnabled = false

                val weight = if (metric) Unit.METRIC.weight else Unit.IMPERIAL.weight
                val length = if (metric) Unit.METRIC.distance else Unit.IMPERIAL.distance.take(2)
                tv_unit1.text = if (byLength) length else weight
                tv_unit2.text = length
                tv_unit3.text = length
                tv_unit4.text = length
                tv_unit5.text = length

                adapterSpinner.unitSelected = second
                adapterSpinner.notifyDataSetChanged()
            }

        })

        viewModel.getForm().observe(viewLifecycleOwner, Observer
        {
            setTextInputLayoutHintText(it)
            setTextInputLayoutVisibility(it)
        })

        field1.initWithListeners(tv_unit1)
        field2.initWithListeners(tv_unit2)
        field3.initWithListeners(tv_unit3)
        field4.initWithListeners(tv_unit4)
        field5.initWithListeners(tv_unit5)

    }

    override fun onResume() {
        super.onResume()

        valueField1?.let { field1.editText?.setText(it.toString()) }
        setScreenConfigurations(
            orientationVertical = true,
            fullScreenMode = false,
            bottomNavViewVisible = false,
            bottomNavViewAnim = false
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        preferences.unregisterOnSharedPreferenceChangeListener(this)
        viewModel.removeSources()
        cancelJobComputingProgress()
    }

    override val hostActivity: Activity?
        get() = activity

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "key_units") {
            val metric = sharedPreferences?.getString(key, null)
                ?.let { it == Unit.METRIC.name }
            metric?.let { viewModel.setUnit(it) }
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun getSelectedForm() {

        val shapeName = arguments?.let {
            CalculatorFragmentArgs.fromBundle(it).shapeSelected
        }
        val form = shapeName?.let { valueOf(it) }
        form?.let {
            viewModel.setForm(it)
            recycler_shapes_marked.scrollToPosition(values().indexOf(it))
        }
    }

    private fun initPreferenceWithDefaultValues() {

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        isUnitDefMetric = preferences.getString(KEY_UNITS, null)?.let { it == Unit.METRIC.name }
        isUnitDefMetric?.let { viewModel.setUnit(it) } ?: viewModel.setUnit(true)
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    private fun RecyclerView.initWithListeners() {

        adapterRecShape = AdapterRecyclerShapes(requireContext(), true) {}
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        this.adapter = adapterRecShape
        this.attachSnapHelperWithListener(
            PagerSnapHelper(),
            onSnapPositionChangeListener = object : OnSnapPositionChangeListener {
                override fun onSnapPositionChange(position: Int) {
                    viewModel.setForm(values()[position])
                }
            })
        indicator.attachToRecyclerView(this)
    }

    private fun RadioGroup.initWithListeners() {

        setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radByLength -> {
                    radByLength.setTextColor(resources.getColor(R.color.colorAccent))
                    radByWeight.setTextColor(resources.getColor(R.color.colorPrimaryLightTrans))
                    viewModel.setType(true)
                }
                R.id.radByWeight -> {
                    radByWeight.setTextColor(resources.getColor(R.color.colorAccent))
                    radByLength.setTextColor(resources.getColor(R.color.colorPrimaryLightTrans))
                    viewModel.setType(false)
                }
            }
        }
        clearCheck()
        radByLength.isChecked = true
    }

    private fun Spinner.initWithListeners() {

        adapterSpinner = AdapterSpinner(
            requireContext(), materials = materials, metric = isUnitDefMetric ?: true
        )
        this.adapter = adapterSpinner
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                viewModel.setSubstance(materials[position])
            }
        }
    }

    private fun startComputingProgressBarLayoutShow() {

        jobComputing = CoroutineScope(Main).launch {
            progressBarLayout.visibility = View.VISIBLE

            while (true) {
                var points = ""
                for (index in 0..3) {
                    tvPoints.text = points
                    delay(300)
                    points += " ."
                }
            }
        }
    }

    private fun cancelJobComputingProgress() {
        progressBarLayout.visibility = View.GONE
        jobComputing?.cancel()
    }

    private fun initButtonCalculate() {
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
                startComputingProgressBarLayoutShow()
                valueField1 = par1
                viewModel.setParameters(par1, par2, par3, par4, par5)
                includePricingOptions()

                viewModel.calculate().observe(viewLifecycleOwner, Observer { model ->

                    model?.let {
                        cancelJobComputingProgress()
                        resultViewModel?.setCalculatedModel(it)
                        try {
                            findNavController().navigate(CalculatorFragmentDirections.actionShowResult())
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                })

            }
        }
    }

    private fun MaterialToolbar.setMenuListener() {

        setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.toSettings -> try {
                    findNavController().navigate(CalculatorFragmentDirections.actionOpenSettings())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                R.id.toSaved -> try {
                    findNavController().navigate(CalculatorFragmentDirections.actionToSavedModels())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun TextInputLayout.initWithListeners(unitTv: TextView) {
        editText?.run {
            doOnTextChanged { text, _, _, _ ->
                isErrorEnabled = false
                text?.run {
                    unitTv.visibility =
                        if (length < 7) View.VISIBLE
                        else View.GONE
                }
            }
            setOnFocusChangeListener { _, hasFocus ->
                unitTv.visibility =
                    if (!hasFocus && text.toString() == "" || text.length > 6) View.GONE
                    else View.VISIBLE
            }
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

    private fun TextInputLayout.hide(unitTv: TextView) {
        clearFocus()
        visibility = View.GONE
        unitTv.visibility = View.GONE
    }

    private fun TextInputLayout.show(unitTv: TextView) {
        unitTv.visibility = View.GONE
        visibility = View.VISIBLE
    }

    private fun setTextInputLayoutVisibility(form: Form) {

        field1.isErrorEnabled = false
        field2.isErrorEnabled = false
        field3.isErrorEnabled = false
        field4.isErrorEnabled = false
        field5.isErrorEnabled = false
        field1.show(tv_unit1)
        field2.show(tv_unit2)

        when (form) {
            ANGLE -> {
                field3.show(tv_unit3);field4.show(tv_unit4);field5.hide(tv_unit5)
            }
            T_BAR -> {
                field3.show(tv_unit3);field4.show(tv_unit4);field5.hide(tv_unit5)
            }
            SQUARE_TUBE -> {
                field3.show(tv_unit3);field4.show(tv_unit4);field5.hide(tv_unit5)
            }
            SQUARE_BAR -> {
                field3.hide(tv_unit3);field4.hide(tv_unit4);field5.hide(tv_unit5)
            }
            ROUND_BAR -> {
                field3.hide(tv_unit3);field4.hide(tv_unit4);field5.hide(tv_unit5)
            }
            PIPE -> {
                field3.show(tv_unit3);field4.hide(tv_unit4);field5.hide(tv_unit5)
            }
            HEXAGONAL_TUBE -> {
                field3.show(tv_unit3);field4.hide(tv_unit4);field5.hide(tv_unit5)
            }
            HEXAGONAL_BAR -> {
                field3.hide(tv_unit3);field4.hide(tv_unit4);field5.hide(tv_unit5)
            }
            HEXAGONAL_HEX -> {
                field3.show(tv_unit3);field4.hide(tv_unit4);field5.hide(tv_unit5)
            }
            FLAT_BAR -> {
                field3.show(tv_unit3);field4.hide(tv_unit4);field5.hide(tv_unit5)
            }
            CHANNEL -> {
                field3.show(tv_unit3);field4.show(tv_unit4);field5.hide(tv_unit5)
            }
            BEAM -> {
                field3.show(tv_unit3);field4.show(tv_unit4);field5.show(tv_unit5)
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

    private fun includePricingOptions() {

        val priceSwitcher = preferences.getBoolean(KEY_PRICE_SWITCHER, false)
        val ratesName = preferences.getString(KEY_RATES, null)
        val currency = if (priceSwitcher) ratesName?.let { Currency.valueOf(ratesName) } else null

        val manuallySwitcher = preferences.getBoolean(KEY_MANUALLY_SWITCHER, false)
        val price: Price? = if (manuallySwitcher) null else null

        viewModel.setPricingOptions(currency, price)
    }
}
