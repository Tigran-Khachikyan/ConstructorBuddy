package com.calcprojects.constructorbuddy.ui.calculator

import android.util.Log
import androidx.lifecycle.*
import com.calcprojects.constructorbuddy.model.figures.Substance
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.model.figures.Form
import com.calcprojects.constructorbuddy.model.figures.Material
import com.calcprojects.constructorbuddy.model.figures.Shape
import com.calcprojects.constructorbuddy.model.units.Unit
import kotlinx.android.synthetic.main.fragment_calculator.*

class CalcViewModel : ViewModel() {

    private val form = MutableLiveData<Form>()
    private val substance = MutableLiveData<Substance>()
    private val typeByLength = MutableLiveData<Boolean>()
    private val params = MutableLiveData<Array<Double?>>()

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

    fun getType(): LiveData<Boolean> {
        if (typeByLength.value == null) typeByLength.value = true
        return typeByLength
    }

    fun setParameters(par1: Double, par2: Double, par3: Double?, par4: Double?, par5: Double?) {
        params.value = arrayOf(par1, par2, par3, par4, par5)
    }

    companion object {
        var modelCalculated: Model? = null
        var byLength: Boolean? = null
    }

    fun calculate(): Boolean {
        val model = getModel(form, substance, typeByLength, params)
        Log.d("ashjhs", "model: $model")

        return model?.let {
            byLength = typeByLength.value
            modelCalculated = it
            true
        } ?: false
    }

    private fun getModel(
        formLD: LiveData<Form>,
        materialLD: LiveData<Substance>,
        byLength: LiveData<Boolean>,
        paramsLD: LiveData<Array<Double?>>
    ): Model? {

        val form = formLD.value
        val mat = materialLD.value
        val type = byLength.value
        val params = paramsLD.value

        var model: Model? = null

        if (form != null && mat != null && type != null && params != null) {
            if (params[0] != null && params[1] != null) {

                val material = Material(mat)
                val unit = Unit.METRIC

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