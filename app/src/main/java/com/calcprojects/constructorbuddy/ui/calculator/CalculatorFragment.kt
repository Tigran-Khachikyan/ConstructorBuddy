package com.calcprojects.constructorbuddy.ui.calculator

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*

import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.figures.Substance
import com.calcprojects.constructorbuddy.model.figures.Form
import com.calcprojects.constructorbuddy.model.figures.Form.*
import com.calcprojects.constructorbuddy.model.price.Currency
import com.calcprojects.constructorbuddy.model.price.Price
import com.calcprojects.constructorbuddy.model.units.Unit
import com.calcprojects.constructorbuddy.ui.*
import com.google.android.material.textfield.TextInputLayout
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class CalculatorFragment : Fragment(), CoroutineScope,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var preferences: SharedPreferences
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Main + Job()

    private lateinit var viewModel: CalculationViewModel
    private var form: Form? = null
    private lateinit var adapterRecShape: AdapterRecyclerShapes
    private val materials by lazy { Substance.values() }
    private var valueField1: Double? = null
    private var unitDefault: Boolean? = null
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()
        viewModel = ViewModelProvider(this).get(CalculationViewModel::class.java)
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        unitDefault = preferences.getString(KEY_UNITS, null)?.let { it == Unit.METRIC.name }
        unitDefault?.let { viewModel.setUnit(it) }
        preferences.registerOnSharedPreferenceChangeListener(this)
        val shapeName = arguments?.let {
            CalculatorFragmentArgs.fromBundle(it).shapeSelected
        }
        viewModel.setType(true)
        form = shapeName?.let { valueOf(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()


        //recycler init
        adapterRecShape = AdapterRecyclerShapes(requireContext(), true) {}
        recycler_shapes_marked.addListeners(adapterRecShape, viewModel, indicator)
        form?.let {
            viewModel.setForm(it)
            recycler_shapes_marked.scrollToPosition(values().indexOf(it))
        }

        //spinner init
        val spinnerAdapter = unitDefault?.let {
            AdapterSpinnerMat(requireContext(), materials = materials, metric = it)
        }
        spinnerAdapter?.let { spinner.addListeners(it, viewModel) }


        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radByLength -> viewModel.setType(true)
                R.id.radByWeight -> viewModel.setType(false)
            }
        }

        btn_calculate.setOnClickListener {
            calculate()
        }

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

                spinnerAdapter?.unitSelected = second
                spinnerAdapter?.notifyDataSetChanged()
            }

        })

        viewModel.getForm().observe(viewLifecycleOwner, Observer
        {
            setTextInputLayoutHintText(it)
            setTextInputLayoutVisibility(it)
        })

        topAppBar_calc_fragment.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toSettings -> findNavController().navigate(CalculatorFragmentDirections.actionOpenSettings())
                R.id.toSaved -> findNavController().navigate(CalculatorFragmentDirections.actionToSavedModels())
            }
            return@setOnMenuItemClickListener false
        }

        field1.addListeners(tv_unit1)
        field2.addListeners(tv_unit2)
        field3.addListeners(tv_unit3)
        field4.addListeners(tv_unit4)
        field5.addListeners(tv_unit5)

    }

    private fun TextInputLayout.addListeners(unitTv: TextView) {
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

    private fun TextInputLayout.hide(unitTv: TextView) = run {
        //  editText?.text?.clear()
        clearFocus()
        visibility = View.GONE
        unitTv.visibility = View.GONE
    }

    private fun TextInputLayout.show(unitTv: TextView) = run {
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

    private fun RecyclerView.addListeners(
        adapter: AdapterRecyclerShapes,
        viewModel: CalculationViewModel? = null,
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

    private fun Spinner.addListeners(
        adapter: AdapterSpinnerMat,
        viewModel: CalculationViewModel? = null,
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
        preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.removeSources()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun configureActivity() {
        activity?.run {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_VISIBLE)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        MainViewModel.showBottomActionView(false)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "key_units") {
            val metric = sharedPreferences?.getString(key, null)
                ?.let { it == Unit.METRIC.name }
            Log.d("asasdad", "metric : $metric")
            metric?.let { viewModel.setUnit(it) }
        }
    }

    private fun includePricingOptions() {

        val priceSwitcher = preferences.getBoolean(KEY_PRICE_SWITCHER, false)
        val ratesName = preferences.getString(KEY_RATES, null)
        val currency = if (priceSwitcher) ratesName?.let { Currency.valueOf(ratesName) } else null

        val manuallySwitcher = preferences.getBoolean(KEY_MANUALLY_SWITCHER, false)
        val price: Price? = if (manuallySwitcher) null else null

        viewModel.setPricingOptions(currency, price)
    }

    private fun calculate() {
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
            includePricingOptions()

            viewModel.calculate().observe(viewLifecycleOwner, Observer { succeed ->
                Log.d("kasynsdf", "succeed: $succeed")
                if (succeed) {
                    try {
                        navController?.navigate(CalculatorFragmentDirections.actionShowResult())
                    } catch (ex: Exception) {
                        Log.d(LOG_EXCEPTION, "ERROR navController: ${ex.message}")
                    }
                }
            })
        }
    }
}
