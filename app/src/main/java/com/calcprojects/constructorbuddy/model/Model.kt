package com.calcprojects.constructorbuddy.model

import com.calcprojects.constructorbuddy.model.Shape.*


data class Model(
    val shape: Shape,
    val material: Material,
    val area: Double,
    val length: Double,
    val weight: Double
) {
    val volume: Double
        get() = area * length

    sealed class Builder(
        private var shape: Shape? = null,
        protected var material: Material? = null,
        protected var area: Double? = null,
        protected var length: Double? = null,
        protected var weight: Double? = null,
        private var par2: Double? = null,
        private var par3: Double? = null,
        private var par4: Double? = null,
        private var par5: Double? = null
    ) {

        fun shape(shape: Shape) = apply { this.shape = shape }
        fun material(material: Material) = apply { this.material = material }
        fun param2(par2: Double) = apply { this.par2 = par2 }
        fun param3(par3: Double?) = apply { this.par3 = par3 }
        fun param4(par4: Double?) = apply { this.par4 = par4 }
        fun param5(par5: Double?) = apply { this.par5 = par5 }

        private fun area() {
            area = shape?.let {
                when (it) {
                    ANGLE -> par2?.run { this * this }
                    BEAM -> par2?.run { this * this }
                    CHANNEL -> par2?.run { this * this }
                    FLAT_BAR -> par2?.run { this * this }
                    HEXAGONAL_BAR -> par2?.run { this * this }
                    HEXAGONAL_HEX -> par2?.run { this * this }
                    HEXAGONAL_TUBE -> par2?.run { this * this }
                    PIPE -> par2?.run { this * this }
                    ROUND_BAR -> par2?.run { this * this }
                    SQUARE_BAR -> par2?.run { this * this }
                    SQUARE_TUBE -> par2?.run { this * this }
                    T_BAR -> par2?.run { this * this }
                }
            }
        }

        protected abstract fun clarify()

        fun build(): Model? {
            area()
            clarify()
            return if (shape != null && material != null && area != null && length != null && weight != null)
                Model(shape!!, material!!, area!!, length!!, weight!!)
            else null
        }

        class ByLength(length: Double) : Builder(length = length) {
            override fun clarify() {
                area?.let { ar ->
                    material?.let { m ->
                        length?.let { weight = ar * m.density * it }
                    }
                }
            }
        }

        class ByWeight(weight: Double) : Builder(weight = weight) {
            override fun clarify() {
                area?.let { ar ->
                    material?.let { m ->
                        weight?.let { w -> length = w / (ar * m.density) }
                    }
                }
            }
        }

    }
}
