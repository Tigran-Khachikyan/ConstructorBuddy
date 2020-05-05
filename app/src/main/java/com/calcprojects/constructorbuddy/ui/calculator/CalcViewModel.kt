package com.calcprojects.constructorbuddy.ui.calculator

import androidx.lifecycle.*
import com.calcprojects.constructorbuddy.model.figures.Substance
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.model.figures.Form
import com.calcprojects.constructorbuddy.model.figures.Material
import com.calcprojects.constructorbuddy.model.figures.Shape
import com.calcprojects.constructorbuddy.model.units.Unit

class CalcViewModel : ViewModel() {

    private val form = MutableLiveData<Form>()
    private val substance = MutableLiveData<Substance>()
    private val typeByLength = MutableLiveData<Boolean>().apply { value = true }
    private val unit = MutableLiveData<Boolean>()
    private val params = MutableLiveData<Array<Double?>>()
    private val typeAndUnit = MediatorLiveData<Pair<Boolean, Boolean>?>()

    fun setForm(form: Form) {
        this.form.value = form
    }

    fun getForm(): LiveData<Form> = form

    fun setSubstance(material: Substance) {
        this.substance.value = material
    }

    fun setType(byLength: Boolean) {
        typeByLength.value = byLength
    }

    fun setParameters(par1: Double, par2: Double, par3: Double?, par4: Double?, par5: Double?) {
        params.value = arrayOf(par1, par2, par3, par4, par5)
    }

    fun setUnit(metric: Boolean) {
        unit.value = metric
    }

    fun getTypeAndUnit(): LiveData<Pair<Boolean, Boolean>?> {
        typeAndUnit.addSource(typeByLength) {
            typeAndUnit.value = combine(typeByLength, unit)
        }
        typeAndUnit.addSource(unit) {
            typeAndUnit.value = combine(typeByLength, unit)
        }
        return typeAndUnit
    }

    fun removeSources() {
        typeAndUnit.removeSource(typeByLength)
        typeAndUnit.removeSource(unit)
    }

    private fun combine(
        typeLD: LiveData<Boolean>, unitLD: LiveData<Boolean>
    ): Pair<Boolean, Boolean>? {
        return unitLD.value?.let { u ->
            typeLD.value?.let { Pair(it, u) }
        }
    }

    companion object {
        var modelCalculated: Model? = null
    }

    fun calculate(): Boolean {
        val model = getModel(form, substance, typeByLength, params, unit)

        return model?.let {
            modelCalculated = it
            true
        } ?: false
    }

    private fun getModel(
        formLD: LiveData<Form>,
        materialLD: LiveData<Substance>,
        byLength: LiveData<Boolean>,
        paramsLD: LiveData<Array<Double?>>,
        unitLD: LiveData<Boolean>
    ): Model? {

        val form = formLD.value
        val mat = materialLD.value
        val type = byLength.value
        val params = paramsLD.value
        val metric = unitLD.value

        var model: Model? = null

        if (form != null && mat != null && type != null && params != null && metric != null) {
            if (params[0] != null && params[1] != null) {

                val unit = if (metric) Unit.METRIC else Unit.IMPERIAL
                val material = Material(mat)
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

                model = if (type) {
                    shape.length = params[0]
                    Model.createByLength(shape, material, unit)
                } else {
                    Model.createByWeight(shape, material, unit, params[0]!!)
                }
            }
        }
        return model
    }

}