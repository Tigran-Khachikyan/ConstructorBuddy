package com.calcprojects.constructorbuddy.ui.calculator

import android.util.Log
import androidx.lifecycle.*
import com.calcprojects.constructorbuddy.model.Material
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.model.Shape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CalcViewModel : ViewModel() {

    private val shape = MutableLiveData<Shape>()
    private val material = MutableLiveData<Material>()
    private val type = MutableLiveData<Boolean>()
    private val params = MutableLiveData<Array<Double?>>()
    private val model = MediatorLiveData<Model>()

    fun setShape(shape: Shape) {
        this.shape.value = shape
    }

    fun getShape(): LiveData<Shape> = shape

    fun setMaterial(material: Material) {
        this.material.value = material
    }

    fun getModel(): LiveData<Model?> {
        viewModelScope.launch {

            Log.d("asafe", "getModel triggered")

            model.addSource(shape) {
                model.value = combine(shape, material, type, params)
            }
            model.addSource(material) {
                model.value = combine(shape, material, type, params)
            }
            model.addSource(params) {
                model.value = combine(shape, material, type, params)
            }
        }
        return model
    }

    fun removeSources() {
        model.removeSource(shape)
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
        shapeLD: LiveData<Shape>,
        materialLD: LiveData<Material>,
        typeLD: LiveData<Boolean>,
        paramsLD: LiveData<Array<Double?>>
    ): Model? {

        Log.d("asafe", "combine triggered")


        val shape = shapeLD.value
        val mat = materialLD.value
        val type = typeLD.value
        val params = paramsLD.value

        return if (shape != null && mat != null && type != null && params != null) {

            Log.d("asafe", "combine inside")

            params[0]?.let { p0 ->
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
            }
        } else null
    }

}