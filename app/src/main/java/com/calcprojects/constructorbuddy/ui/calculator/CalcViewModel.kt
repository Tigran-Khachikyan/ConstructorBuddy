package com.calcprojects.constructorbuddy.ui.calculator

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.calcprojects.constructorbuddy.data.Repository
import com.calcprojects.constructorbuddy.data.api_currency.ApiCurrency
import com.calcprojects.constructorbuddy.data.api_currency.RetrofitService
import com.calcprojects.constructorbuddy.data.db.Database
import com.calcprojects.constructorbuddy.model.figures.Substance
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.model.figures.Form
import com.calcprojects.constructorbuddy.model.figures.Material
import com.calcprojects.constructorbuddy.model.figures.Shape
import com.calcprojects.constructorbuddy.model.price.Currency
import com.calcprojects.constructorbuddy.model.price.Price
import com.calcprojects.constructorbuddy.model.units.Unit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalcViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: Repository by lazy {
        Repository(
            application,
            RetrofitService.createService(ApiCurrency::class.java),
            Database(application).getModelDao()
        )
    }

    private val form = MutableLiveData<Form>()
    private val substance = MutableLiveData<Substance>()
    private val typeByLength = MutableLiveData<Boolean>().apply { value = true }
    private val unit = MutableLiveData<Boolean>()
    private val params = MutableLiveData<Array<Double?>>()
    private val currency = MutableLiveData<Currency>()
    private val price = MutableLiveData<Price>()
    private val typeAndUnit = MediatorLiveData<Pair<Boolean, Boolean>?>()

    private val succeed = MutableLiveData<Boolean>().apply { value = false }

    fun setForm(_form: Form) = run { form.value = _form }

    fun getForm(): LiveData<Form> = form

    fun setSubstance(_substance: Substance) = run { substance.value = _substance }

    fun setType(byLength: Boolean) = run { typeByLength.value = byLength }

    fun setParameters(par1: Double, par2: Double, par3: Double?, par4: Double?, par5: Double?) {
        params.value = arrayOf(par1, par2, par3, par4, par5)
    }

    fun setUnit(metric: Boolean) = run { unit.value = metric }

    fun getTypeAndUnit(): LiveData<Pair<Boolean, Boolean>?> {

        typeAndUnit.addSource(typeByLength) {
            typeAndUnit.value = combineTypeAndUnit(typeByLength, unit)
        }
        typeAndUnit.addSource(unit) {
            typeAndUnit.value = combineTypeAndUnit(typeByLength, unit)
        }
        return typeAndUnit
    }

    fun removeSources() {
        typeAndUnit.removeSource(typeByLength)
        typeAndUnit.removeSource(unit)
    }

    private fun combineTypeAndUnit(
        typeLD: LiveData<Boolean>, unitLD: LiveData<Boolean>
    ): Pair<Boolean, Boolean>? {
        return unitLD.value?.let { u ->
            typeLD.value?.let { Pair(it, u) }
        }
    }

    companion object {
        var modelCalculated: Model? = null
    }

    fun calculate(): LiveData<Boolean> {

        viewModelScope.launch(Dispatchers.IO) {
            val model = getModel(form, substance, typeByLength, params, unit, currency, price)
            Log.d("kasynsdf", "model in scope: $model")

            model?.let {
                modelCalculated = it
                withContext(Dispatchers.Main) { succeed.value = true }
            }
        }
        return succeed
    }

    private suspend fun getModel(
        _form: LiveData<Form>,
        _substance: LiveData<Substance>,
        _byLength: LiveData<Boolean>,
        _params: LiveData<Array<Double?>>,
        _unitMetric: LiveData<Boolean>,
        _currency: LiveData<Currency>,
        _price: LiveData<Price>
    ): Model? {

        val form = _form.value
        val sub = _substance.value
        val type = _byLength.value
        val params = _params.value
        val metric = _unitMetric.value
        val currency = _currency.value
        val price = _price.value

        var model: Model? = null

        if (form != null && sub != null && type != null && params != null && metric != null) {
            if (params[0] != null && params[1] != null) {

                val unit = if (metric) Unit.METRIC else Unit.IMPERIAL
                val shape = Shape(form)
                when (shape.form) {
                    Form.SQUARE_TUBE, Form.ANGLE, Form.CHANNEL, Form.T_BAR -> {
                        shape.run { width = params[1]; height = params[2]; thickness = params[3] }
                    }
                    Form.SQUARE_BAR -> {
                        shape.side = params[1]
                    }
                    Form.ROUND_BAR -> {
                        shape.diameter = params[1]
                    }
                    Form.PIPE -> {
                        shape.run { diameter = params[1]; thickness = params[2] }
                    }
                    Form.HEXAGONAL_TUBE -> {
                        shape.run { diameter = params[1]; side = params[2] }
                    }
                    Form.HEXAGONAL_BAR -> {
                        shape.height = params[1]
                    }
                    Form.HEXAGONAL_HEX -> {
                        shape.run { width = params[1]; thickness = params[2] }
                    }
                    Form.FLAT_BAR -> {
                        shape.run {
                            width = params[1]; height = params[2]
                        }
                    }
                    Form.BEAM -> {
                        shape.run {
                            width = params[1]; height = params[2];
                            thickness = params[3]; thickness2 = params[4]
                        }
                    }
                }

                val material = currency?.let { cur ->
                    price?.let { repo.getManuallyPricedMaterial(sub, cur, it) }
                        ?: repo.getAutoPricedMaterial(sub, cur)
                }
                Log.d("kasynsdf", "price: $price")
                Log.d("kasynsdf", "currency: $currency")
                Log.d("kasynsdf", "material: $material")

                model = if (type) {
                    shape.length = params[0]

                    material?.let { Model.createByLength(shape, it, unit) }
                } else {
                    material?.let { Model.createByWeight(shape, it, unit, params[0]!!) }
                }
            }
        }
        return model
    }


    // ---------------------------------------------------------------------------------------


    fun setPricingOptions(_selectedCurrency: Currency?, _priceManually: Price?) {
        currency.value = _selectedCurrency
        price.value = _priceManually
    }

}