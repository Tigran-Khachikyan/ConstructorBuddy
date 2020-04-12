package com.calcprojects.constructorbuddy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.shapes.ShapesFragment

class CalculateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)

        supportFragmentManager.beginTransaction().replace(R.id.frame_calculation, ShapesFragment()).commit()
    }
}
