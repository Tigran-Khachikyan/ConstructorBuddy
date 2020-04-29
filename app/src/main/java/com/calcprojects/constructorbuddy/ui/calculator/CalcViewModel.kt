package com.calcprojects.constructorbuddy.ui.calculator

import android.util.Log
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
    private val type = MutableLiveData<Boolean>()
    private val params = MutableLiveData<Array<Double?>>()

    fun setForm(form: Form) {
        this.form.value = form
    }

    fun getForm(): LiveData<Form> = form

    fun setSubstance(material: Substance) {
        this.substance.value = material
    }

    fun setType(byLength: Boolean) {
        type.value = byLength
    }

    fun getType(): LiveData<Boolean> {
        if (type.value == null) type.value = true
        return type
    }

    fun setParameters(par1: Double, par2: Double, par3: Double?, par4: Double?, par5: Double?) {
        params.value = arrayOf(par1, par2, par3, par4, par5)
    }

    companion object {
        var modelCalculated: Model? = null
        var byLength: Boolean? = null
    }

    fun calculate(): Boolean {
        val model = getModel(form, substance, type, params)
        Log.d("ashjhs","model: $model")

        return model?.let {
            byLength = type.value
            modelCalculated = it
            true
        } ?: false
    }

    private fun getModel(
        formLD: LiveData<Form>,
        materialLD: LiveData<Substance>,
        typeLD: LiveData<Boolean>,
        paramsLD: LiveData<Array<Double?>>
    ): Model? {

        val form = formLD.value
        val mat = materialLD.value
        val type = typeLD.value
        val params = paramsLD.value

        var model: Model? = null

        if (form != null && mat != null && type != null && params != null) {
            if (params[0] != null && params[1] != null) {

                Log.d("ashjhs","if All")

                val material = Material(mat)
                val unit = Unit.METRIC
                model = if (type) {
                    val shape = Shape(form, params[0], params[1], params[2], params[3], params[4])
                    Model.createByLength(shape, material, unit)
                } else {
                    val shape = Shape(form, null, params[1], params[2], params[3], params[4])
                    Log.d("ashjhs","shape hhha: $shape")
                    Log.d("ashjhs","params[0] hhha: ${params[0]}")
                    Model.createByWeight(shape, material, unit, params[0]!!)

                }
                Log.d("ashjhs","model after: $model")

            }
        }
        return model
    }

}