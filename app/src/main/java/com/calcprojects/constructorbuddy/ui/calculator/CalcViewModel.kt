package com.calcprojects.constructorbuddy.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.calcprojects.constructorbuddy.model.Material
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.model.Shape

class CalcViewModel : ViewModel() {

    private val shape = MutableLiveData<Shape>()
    private val material = MutableLiveData<Material>()
    private val model = MutableLiveData<Model>()

    fun setShape(shape: Shape) {
        this.shape.value = shape
    }

    fun getShape(): LiveData<Shape> = shape

    fun setMaterial(material: Material) {
        this.material.value = material
    }

    fun getMaterial(): LiveData<Material> = material

    fun setModel(model: Model) {
        this.model.value = model
    }

    fun getModel(): LiveData<Model> = model

}