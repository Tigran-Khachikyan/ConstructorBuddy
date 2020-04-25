package com.calcprojects.constructorbuddy.ui.calculator

import android.util.Log
import androidx.lifecycle.*
import com.calcprojects.constructorbuddy.model.figures.Substance
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.model.figures.Form
import kotlinx.coroutines.launch

class CalcViewModel : ViewModel() {

    private val form = MutableLiveData<Form>()
    private val material = MutableLiveData<Substance>()
    private val type = MutableLiveData<Boolean>()
    private val params = MutableLiveData<Array<Double?>>()
    private val model = MediatorLiveData<Model>()

    fun setForm(form: Form) {
        this.form.value = form
    }

    fun getForm(): LiveData<Form> = form

    fun setMaterial(material: Substance) {
        this.material.value = material
    }

    fun getModel(): LiveData<Model?> {
        viewModelScope.launch {

            Log.d("asafe", "getModel triggered")

            model.addSource(form) {
                model.value = combine(form, material, type, params)
            }
            model.addSource(material) {
                model.value = combine(form, material, type, params)
            }
            model.addSource(params) {
                model.value = combine(form, material, type, params)
            }
        }
        return model
    }

    fun removeSources() {
        model.removeSource(form)
        model.removeSource(material)
        model.removeSource(params)
    }

    fun setType(byLength: Boolean) {
        type.value = byLength
    }

    fun getType(): LiveData<Boolean> {
        if (type.value == null) type.value = true
        return type
    }

    fun setParameters(par1: Double, par2: Double, par3: Double?, par4: Double?, par5: Double?) {
        Log.d("asafe", "in setParameters triggered")

        params.value = arrayOf(par1, par2, par3, par4, par5)
    }

    private fun combine(
        formLD: LiveData<Form>,
        materialLD: LiveData<Substance>,
        typeLD: LiveData<Boolean>,
        paramsLD: LiveData<Array<Double?>>
    ): Model? {

        val form = formLD.value
        val mat = materialLD.value
        val type = typeLD.value
        val params = paramsLD.value

        return if (form != null && mat != null && type != null && params != null) {

           /* params[0]?.let { p0 ->
                params[1]?.let { p1 ->
                    if (type)
                        Model.Builder
                            .ByLength(p0)
                            .shape(shape)
                            .material(mat)
                            .param2(p1).param3(params[2]).param4(params[3]).param5(params[4])
                            .build()
                    else Model.Builder
                        .ByWeight(p0)
                        .shape(shape)
                        .material(mat)
                        .param2(p1).param3(params[2]).param4(params[3]).param5(params[4])
                        .build()
                }
            }*/
            null
        } else null
    }

}